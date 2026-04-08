package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 酒店数据访问接口
 * 提供酒店相关的数据库操作
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    /**
     * 根据城市查询酒店（分页）
     * @param city 城市名称
     * @param pageable 分页信息
     * @return 分页酒店列表
     */
    Page<Hotel> findByCity(String city, Pageable pageable);

    /**
     * 根据酒店名称模糊查询（分页）
     * @param name 酒店名称
     * @param pageable 分页信息
     * @return 分页酒店列表
     */
    Page<Hotel> findByNameContaining(String name, Pageable pageable);

    /**
     * 根据星级查询酒店
     * @param starLevel 星级
     * @return 酒店列表
     */
    List<Hotel> findByStarLevelGreaterThanEqual(Integer starLevel);

    /**
     * 获取评分最高的前 10 家酒店
     * @return 酒店列表
     */
    List<Hotel> findTop10ByOrderByRatingDesc();
}
