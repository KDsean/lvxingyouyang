package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.LoginRequest;
import com.lvxingyouyang.dto.LoginResponse;
import com.lvxingyouyang.dto.RegisterRequest;
import com.lvxingyouyang.dto.UserDTO;

/**
 * 认证服务接口
 * 提供用户注册、登录、信息管理等功能
 */
public interface IAuthService {
    /**
     * 用户注册
     * @param request 注册请求（包含用户名、邮箱、密码等）
     * @return 登录响应（包含 Token 和用户信息）
     */
    LoginResponse register(RegisterRequest request);

    /**
     * 用户登录
     * @param request 登录请求（包含用户名和密码）
     * @return 登录响应（包含 Token 和用户信息）
     */
    LoginResponse login(LoginRequest request);

    /**
     * 获取当前用户信息
     * @param userId 用户 ID
     * @return 用户数据传输对象
     */
    UserDTO getCurrentUser(Long userId);

    /**
     * 更新用户信息
     * @param userId 用户 ID
     * @param userDTO 用户数据传输对象
     * @return 更新后的用户信息
     */
    UserDTO updateProfile(Long userId, UserDTO userDTO);
}
