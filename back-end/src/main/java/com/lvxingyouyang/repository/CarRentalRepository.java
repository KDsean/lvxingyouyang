package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.CarRental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 租车数据访问接口
 * 提供租车相关的数据库操作
 */
@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, Long> {
    /**
     * 根据地点查询租车（分页）
     * @param location 地点
     * @param pageable 分页信息
     * @return 分页租车列表
     */
    Page<CarRental> findByLocation(String location, Pageable pageable);
}
