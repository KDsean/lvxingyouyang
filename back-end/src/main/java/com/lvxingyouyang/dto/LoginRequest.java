package com.lvxingyouyang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求 DTO
 * 用于接收用户登录请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;
}
