package com.lvxingyouyang.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库 users 表
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /** 用户 ID，主键自增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户名，唯一且不为空 */
    @Column(unique = true, nullable = false)
    private String username;

    /** 邮箱，唯一且不为空 */
    @Column(unique = true, nullable = false)
    private String email;

    /** 密码，不为空（已加密） */
    @Column(nullable = false)
    private String password;

    /** 电话号码 */
    private String phone;

    /** 头像 URL */
    private String avatar;

    /** 创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 更新时间 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 创建前回调
     * 自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 更新前回调
     * 自动更新修改时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
