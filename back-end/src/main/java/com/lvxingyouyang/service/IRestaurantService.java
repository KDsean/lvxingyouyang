package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Restaurant;
import java.util.List;

/**
 * 餐厅服务接口
 * 提供餐厅搜索、查询、推荐等功能
 */
public interface IRestaurantService {
    /**
     * 搜索餐厅
     * @param city 城市名称
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页餐厅列表
     */
    PageResponse<Restaurant> searchRestaurants(String city, String keyword, Integer page, Integer pageSize);

    /**
     * 获取餐厅详情
     * @param id 餐厅 ID
     * @return 餐厅信息
     */
    Restaurant getRestaurantDetail(Long id);

    /**
     * 获取热门餐厅
     * @param city 城市名称
     * @return 热门餐厅列表
     */
    List<Restaurant> getPopularRestaurants(String city);

    /**
     * 获取推荐餐厅
     * @param city 城市名称
     * @return 推荐餐厅列表
     */
    List<Restaurant> getRecommendedRestaurants(String city);
}
