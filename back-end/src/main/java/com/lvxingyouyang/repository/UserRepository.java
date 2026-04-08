package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 提供用户相关的数据库操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息（Optional）
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息（Optional）
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户信息（Optional）
     */
    Optional<User> findByPhone(String phone);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return true 表示存在，false 表示不存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return true 表示存在，false 表示不存在
     */
    boolean existsByEmail(String email);
}
