package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Guide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 攻略数据访问接口
 * 提供攻略相关的数据库操作
 */
@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    /**
     * 根据目的地查询攻略（分页）
     * @param destination 目的地
     * @param pageable 分页信息
     * @return 分页攻略列表
     */
    Page<Guide> findByDestination(String destination, Pageable pageable);

    /**
     * 根据标题模糊查询攻略（分页）
     * @param title 攻略标题
     * @param pageable 分页信息
     * @return 分页攻略列表
     */
    Page<Guide> findByTitleContaining(String title, Pageable pageable);

    /**
     * 根据标题和目的地组合查询（分页）
     * @param title 攻略标题
     * @param destination 目的地
     * @param pageable 分页信息
     * @return 分页攻略列表
     */
    Page<Guide> findByTitleContainingAndDestination(String title, String destination, Pageable pageable);

    /**
     * 获取点赞数最多的前 10 篇攻略
     * @return 攻略列表
     */
    List<Guide> findTop10ByOrderByLikeCountDesc();
}
