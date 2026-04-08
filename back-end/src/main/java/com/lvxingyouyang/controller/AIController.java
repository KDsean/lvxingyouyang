package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * AI 规划服务控制器
 * 作为 Spring Boot 与 Python LangChain 服务之间的代理层
 * 前端 → Spring Boot(/api/ai/...) → Python LangChain(localhost:8001)
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    @Value("${ai.python.url:http://localhost:8001}")
    private String pythonAiUrl;

    private final RestTemplate restTemplate;

    /**
     * 普通对话接口（非流式）
     * POST /api/ai/chat
     * 转发到 Python: POST http://localhost:8001/chat
     */
    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, Object> body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Object> response = restTemplate.postForEntity(
                pythonAiUrl + "/chat", entity, Object.class
            );
            return ResponseEntity.ok(ApiResponse.success(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("AI 服务异常: " + e.getMessage()));
        }
    }

    /**
     * 流式对话接口（SSE）
     * POST /api/ai/chat/stream
     * 将 Python LangChain 的 SSE 流透传给前端
     */
    @PostMapping("/chat/stream")
    public SseEmitter chatStream(@RequestBody Map<String, Object> body) {
        // 超时设置为 5 分钟（LangChain 可能需要较长时间）
        SseEmitter emitter = new SseEmitter(300_000L);

        Thread thread = new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(pythonAiUrl + "/chat/stream");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10_000);
                conn.setReadTimeout(300_000);

                // 序列化请求体
                String jsonBody = toJson(body);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.getBytes("UTF-8"));
                }

                int status = conn.getResponseCode();
                if (status != 200) {
                    emitter.completeWithError(new RuntimeException("Python AI 服务返回: " + status));
                    return;
                }

                // 逐行读取 SSE 并转发给前端
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            // 去掉 "data: " 前缀，SseEmitter.event().data() 会自动加回
                            String data = line.substring(6);
                            emitter.send(SseEmitter.event().data(data));
                            if ("[DONE]".equals(data.trim())) {
                                break;
                            }
                        } else if (!line.isEmpty()) {
                            // 非 data: 行（如空行）直接忽略
                        }
                    }
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            } finally {
                if (conn != null) conn.disconnect();
            }
        });
        thread.setDaemon(true);
        thread.start();

        return emitter;
    }

    /**
     * 健康检查：检测 Python LangChain 服务是否在线
     * GET /api/ai/health
     */
    @GetMapping("/health")
    public ApiResponse<?> health() {
        try {
            ResponseEntity<String> res = restTemplate.getForEntity(
                pythonAiUrl + "/health", String.class
            );
            return ApiResponse.success(Map.of(
                "status", "ok",
                "pythonService", res.getStatusCode().is2xxSuccessful() ? "online" : "offline"
            ));
        } catch (Exception e) {
            return ApiResponse.success(Map.of(
                "status", "ok",
                "pythonService", "offline",
                "message", e.getMessage()
            ));
        }
    }

    /** 简单 JSON 序列化（避免额外依赖） */
    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        map.forEach((k, v) -> {
            sb.append('"').append(k).append('"').append(':');
            if (v instanceof String) {
                sb.append('"').append(((String) v)
                    .replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                ).append('"');
            } else {
                sb.append(v);
            }
            sb.append(',');
        });
        if (sb.charAt(sb.length() - 1) == ',') sb.deleteCharAt(sb.length() - 1);
        sb.append('}');
        return sb.toString();
    }
}
