package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 目的地数据访问接口
 * 提供目的地相关的数据库操作
 */
@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    /**
     * 根据国家查询目的地（分页）
     * @param country 国家名称
     * @param pageable 分页信息
     * @return 分页目的地列表
     */
    Page<Destination> findByCountry(String country, Pageable pageable);

    /**
     * 根据目的地名称模糊查询（分页）
     * @param name 目的地名称
     * @param pageable 分页信息
     * @return 分页目的地列表
     */
    Page<Destination> findByNameContaining(String name, Pageable pageable);

    /**
     * 根据国家和名称模糊查询（分页）
     * @param country 国家名称
     * @param name 目的地名称
     * @param pageable 分页信息
     * @return 分页目的地列表
     */
    Page<Destination> findByCountryAndNameContaining(String country, String name, Pageable pageable);

    /**
     * 获取热度最高的前 10 个目的地
     * @return 目的地列表
     */
    List<Destination> findTop10ByOrderByPopularityScoreDesc();
}
