package com.lvxingyouyang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一 API 响应类
 * 所有 API 响应都使用此格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    /** 响应状态码（200 表示成功，其他表示失败） */
    private Integer code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /**
     * 构建成功响应
     * @param data 响应数据
     * @return ApiResponse 对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .build();
    }

    /**
     * 构建成功响应（带自定义消息）
     * @param message 自定义消息
     * @param data 响应数据
     * @return ApiResponse 对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 构建错误响应
     * @param code 错误状态码
     * @param message 错误消息
     * @return ApiResponse 对象
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    /**
     * 构建错误响应（默认状态码 500）
     * @param message 错误消息
     * @return ApiResponse 对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .code(500)
                .message(message)
                .build();
    }
}
