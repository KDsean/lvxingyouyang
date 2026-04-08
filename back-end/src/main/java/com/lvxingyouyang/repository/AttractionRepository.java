package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Attraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 景点数据访问接口
 * 提供景点相关的数据库操作
 */
@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    /**
     * 根据城市查询景点（分页）
     * @param city 城市名称
     * @param pageable 分页信息
     * @return 分页景点列表
     */
    Page<Attraction> findByCity(String city, Pageable pageable);

    /**
     * 根据景点名称模糊查询（分页）
     * @param name 景点名称
     * @param pageable 分页信息
     * @return 分页景点列表
     */
    Page<Attraction> findByNameContaining(String name, Pageable pageable);

    /**
     * 获取评分最高的前 10 个景点
     * @return 景点列表
     */
    List<Attraction> findTop10ByOrderByRatingDesc();
}
