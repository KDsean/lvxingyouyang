package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.dto.LoginRequest;
import com.lvxingyouyang.dto.RegisterRequest;
import com.lvxingyouyang.dto.UserDTO;
import com.lvxingyouyang.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理用户注册、登录、信息管理等请求
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    /**
     * 用户注册
     * @param request 注册请求
     * @return 登录响应（包含 Token 和用户信息）
     */
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest request) {
        return ApiResponse.success(authService.register(request));
    }

    /**
     * 用户登录
     * @param request 登录请求
     * @return 登录响应（包含 Token 和用户信息）
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    /**
     * 获取当前用户信息
     * @param authentication 认证信息（包含用户 ID）
     * @return 用户信息
     */
    @GetMapping("/user")
    public ApiResponse<?> getCurrentUser(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(authService.getCurrentUser(userId));
    }

    /**
     * 更新用户信息
     * @param authentication 认证信息
     * @param userDTO 用户数据
     * @return 更新后的用户信息
     */
    @PutMapping("/profile")
    public ApiResponse<?> updateProfile(Authentication authentication, @RequestBody UserDTO userDTO) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(authService.updateProfile(userId, userDTO));
    }

    /**
     * 用户登出
     * @return 登出成功消息
     */
    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        return ApiResponse.success("登出成功");
    }
}
