# 旅行有样 AI Agent

基于 **LangChain 1.0** + **MCP** 的旅行规划 AI Agent，集成三个 MCP 服务：

| MCP 服务 | 功能 |
|---------|------|
| 🗺️ 高德地图 | POI搜索、路线规划、天气查询、地理编码 |
| 🚄 12306 | 火车票查询、余票、时刻表、车站信息 |
| ✈️ 飞常准 | 航班查询、实时动态、机场信息 |

## 项目结构

```
agent/
├── travel_agent.py  # Agent 核心逻辑（MCP工具加载 + 流式对话）
├── server.py        # FastAPI Web服务（供 Spring Boot 调用）
├── requirements.txt # Python 依赖
├── .env.example     # 环境变量示例
└── .env             # 实际配置（勿提交 Git）
```

## 快速开始

### 1. 安装依赖

```bash
cd agent
pip install -r requirements.txt
```

> 需要 Node.js（用于运行 MCP 服务）：`node --version` 确认已安装

### 2. 配置环境变量

```bash
cp .env .env
```

编辑 `.env`：

```env
# 大模型配置（支持 OpenAI / DeepSeek / 通义千问等兼容接口）
OPENAI_API_KEY=sk-xxx
OPENAI_BASE_URL=https://api.deepseek.com/v1   # DeepSeek 示例
MODEL_NAME=deepseek-chat

# 高德地图 Key（https://lbs.amap.com/）
AMAP_API_KEY=your_amap_key

# 飞常准 Key（https://www.variflight.com/）
VARIFLIGHT_API_KEY=your_variflight_key
```

### 3. 启动服务

```bash
# 方式一：直接运行
python server.py

# 方式二：uvicorn（推荐）
uvicorn server:app --host 0.0.0.0 --port 8001
```

服务启动后访问 http://localhost:8001/health 确认正常。

### 4. 本地命令行测试

```bash
python travel_agent.py
```

## API 接口

### 健康检查
```
GET /health
→ {"status": "ok", "service": "travel-agent"}
```

### 普通对话（非流式）
```
POST /chat
Body: {
  "message": "我想从北京去上海，推荐高铁还是飞机？",
  "history": [],
  "sessionId": "user123"
}
→ {"content": "AI回复...", "sessionId": "user123"}
```

### 流式对话（SSE）
```
POST /chat/stream
Body: 同上
→ text/event-stream
   data: {"content": "根"}
   data: {"content": "据"}
   ...
   data: [DONE]
```

## 与 Spring Boot 的通信架构

```
前端 Vue3 (localhost:3000)
    │ POST /api/ai/chat/stream
    ▼
Spring Boot (localhost:8080)
    │ 转发到 Python
    ▼
Python FastAPI (localhost:8001)
    │ 调用 MCP 工具
    ├── 高德地图 MCP (npx @amap/amap-maps)
    ├── 12306 MCP (npx @Joooook/12306-mcp)
    └── 飞常准 MCP (npx @variflight-ai/variflight-mcp)
```

## 支持的大模型

| 模型 | BASE_URL | MODEL_NAME |
|------|---------|------------|
| OpenAI GPT-4o | https://api.openai.com/v1 | gpt-4o |
| DeepSeek | https://api.deepseek.com/v1 | deepseek-chat |
| 通义千问 | https://dashscope.aliyuncs.com/compatible-mode/v1 | qwen-max |
| 智谱 GLM-4 | https://open.bigmodel.cn/api/paas/v4 | glm-4 |
