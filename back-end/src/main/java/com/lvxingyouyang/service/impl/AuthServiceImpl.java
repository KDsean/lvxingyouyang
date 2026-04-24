package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.LoginRequest;
import com.lvxingyouyang.dto.LoginResponse;
import com.lvxingyouyang.dto.RegisterRequest;
import com.lvxingyouyang.dto.UserDTO;
import com.lvxingyouyang.entity.User;
import com.lvxingyouyang.repository.UserRepository;
import com.lvxingyouyang.security.JwtTokenProvider;
import com.lvxingyouyang.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 认证服务实现类
 * 实现用户注册、登录、信息管理等功能
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 用户注册
     * 1. 检查用户名是否已存在
     * 2. 检查邮箱是否已存在
     * 3. 密码加密存储
     * 4. 生成 JWT Token
     */
    @Override
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 创建新用户，密码使用 BCrypt 加密
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();

        user = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(user.getId());

        return LoginResponse.builder()
                .token(token)
                .user(convertToDTO(user))
                .build();
    }

    /**
     * 用户登录
     * 1. 支持用户名、邮箱、手机号三种方式登录
     * 2. 验证密码是否正确
     * 3. 生成 JWT Token
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        String loginId = request.getUsername();
        
        // 依次尝试用户名、邮箱、手机号查找用户
        User user = userRepository.findByUsername(loginId)
                .or(() -> userRepository.findByEmail(loginId))
                .or(() -> userRepository.findByPhone(loginId))
                .orElseThrow(() -> new RuntimeException("用户不存在，请检查用户名/邮箱/手机号"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtTokenProvider.generateToken(user.getId());

        return LoginResponse.builder()
                .token(token)
                .user(convertToDTO(user))
                .build();
    }

    /**
     * 获取当前用户信息
     */
    @Override
    public UserDTO getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToDTO(user);
    }

    /**
     * 更新用户信息
     * 支持更新电话和头像
     */
    @Override
    public UserDTO updateProfile(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (userDTO.getUsername() != null && !userDTO.getUsername().isBlank()
                && !Objects.equals(user.getUsername(), userDTO.getUsername())) {
            userRepository.findByUsername(userDTO.getUsername())
                    .filter(existingUser -> !Objects.equals(existingUser.getId(), userId))
                    .ifPresent(existingUser -> {
                        throw new RuntimeException("用户名已存在");
                    });
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().isBlank()
                && !Objects.equals(user.getEmail(), userDTO.getEmail())) {
            userRepository.findByEmail(userDTO.getEmail())
                    .filter(existingUser -> !Objects.equals(existingUser.getId(), userId))
                    .ifPresent(existingUser -> {
                        throw new RuntimeException("邮箱已存在");
                    });
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhone() != null) {
            user.setPhone(userDTO.getPhone());
        }
        if (userDTO.getAvatar() != null) {
            user.setAvatar(userDTO.getAvatar());
        }

        user = userRepository.save(user);
        return convertToDTO(user);
    }

    /**
     * 将 User 实体转换为 UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .createdAt(user.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }
}
