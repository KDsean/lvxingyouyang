package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.TripPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 行程数据访问接口
 * 提供行程相关的数据库操作
 */
@Repository
public interface TripPlanRepository extends JpaRepository<TripPlan, Long> {
    /**
     * 根据用户 ID 查询行程（分页）
     * @param userId 用户 ID
     * @param pageable 分页信息
     * @return 分页行程列表
     */
    Page<TripPlan> findByUserId(Long userId, Pageable pageable);
}
