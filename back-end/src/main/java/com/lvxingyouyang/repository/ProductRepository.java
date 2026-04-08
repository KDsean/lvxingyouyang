package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 商品数据访问接口
 * 提供商品相关的数据库操作
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * 根据分类查询商品（分页）
     * @param category 商品分类
     * @param pageable 分页信息
     * @return 分页商品列表
     */
    Page<Product> findByCategory(String category, Pageable pageable);

    /**
     * 根据商品名称模糊查询（分页）
     * @param name 商品名称
     * @param pageable 分页信息
     * @return 分页商品列表
     */
    Page<Product> findByNameContaining(String name, Pageable pageable);

    /**
     * 获取销售量最高的前 10 个商品
     * @return 商品列表
     */
    List<Product> findTop10ByOrderBySalesDesc();
}
