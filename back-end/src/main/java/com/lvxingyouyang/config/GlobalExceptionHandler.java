package com.lvxingyouyang.config;

import com.lvxingyouyang.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常处理器
 * 统一处理应用中的异常，返回统一格式的错误响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 RuntimeException
     * @param ex RuntimeException 异常
     * @param request Web 请求
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ApiResponse<?> response = ApiResponse.error(400, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理所有其他异常
     * @param ex 异常
     * @param request Web 请求
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex, WebRequest request) {
        ApiResponse<?> response = ApiResponse.error(500, "服务器内部错误");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
