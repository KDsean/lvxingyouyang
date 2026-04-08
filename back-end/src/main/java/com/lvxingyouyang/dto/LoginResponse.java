package com.lvxingyouyang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 DTO
 * 用于返回登录结果（包含 Token 和用户信息）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    /** JWT Token */
    private String token;

    /** 用户信息 */
    private UserDTO user;
}
