package com.lvxingyouyang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求 DTO
 * 用于接收用户注册请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    /** 用户名 */
    private String username;

    /** 邮箱 */
    private String email;

    /** 密码 */
    private String password;

    /** 电话号码（可选） */
    private String phone;
}
