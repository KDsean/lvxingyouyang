package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 订单数据访问接口
 * 提供订单相关的数据库操作
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 根据用户 ID 查询订单（分页）
     * @param userId 用户 ID
     * @param pageable 分页信息
     * @return 分页订单列表
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据用户 ID 和订单类型查询订单（分页）
     * @param userId 用户 ID
     * @param type 订单类型
     * @param pageable 分页信息
     * @return 分页订单列表
     */
    Page<Order> findByUserIdAndType(Long userId, String type, Pageable pageable);

    /**
     * 根据用户 ID 和订单状态查询订单（分页）
     * @param userId 用户 ID
     * @param status 订单状态
     * @param pageable 分页信息
     * @return 分页订单列表
     */
    Page<Order> findByUserIdAndStatus(Long userId, String status, Pageable pageable);
}
