package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Product;
import java.util.List;

/**
 * 商品服务接口
 * 提供商品搜索、查询、推荐等功能
 */
public interface IProductService {
    /**
     * 获取商品列表
     * @param category 分类
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页商品列表
     */
    PageResponse<Product> getProducts(String category, String keyword, Integer page, Integer pageSize);

    /**
     * 获取商品详情
     * @param id 商品 ID
     * @return 商品信息
     */
    Product getProductDetail(Long id);

    /**
     * 获取热门商品
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<Product> getPopularProducts(Integer limit);

    /**
     * 获取推荐商品
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getRecommendedProducts(Integer limit);
}
