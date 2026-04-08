package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 餐厅数据访问接口
 * 提供餐厅相关的数据库操作
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    /**
     * 根据城市查询餐厅（分页）
     * @param city 城市名称
     * @param pageable 分页信息
     * @return 分页餐厅列表
     */
    Page<Restaurant> findByCity(String city, Pageable pageable);

    /**
     * 根据餐厅名称模糊查询（分页）
     * @param name 餐厅名称
     * @param pageable 分页信息
     * @return 分页餐厅列表
     */
    Page<Restaurant> findByNameContaining(String name, Pageable pageable);

    /**
     * 获取评分最高的前 10 家餐厅
     * @return 餐厅列表
     */
    List<Restaurant> findTop10ByOrderByRatingDesc();
}
