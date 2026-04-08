package com.lvxingyouyang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户数据传输对象 (DTO)
 * 用于在 API 中传输用户信息（不包含敏感信息如密码）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    /** 用户 ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 邮箱 */
    private String email;

    /** 电话号码 */
    private String phone;

    /** 头像 URL */
    private String avatar;

    /** 创建时间 */
    private String createdAt;
}
