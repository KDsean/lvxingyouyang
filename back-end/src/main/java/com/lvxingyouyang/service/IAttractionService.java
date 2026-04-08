package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Attraction;
import java.util.List;

/**
 * 景点服务接口
 * 提供景点搜索、查询、推荐等功能
 */
public interface IAttractionService {
    /**
     * 搜索景点
     * @param city 城市名称
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页景点列表
     */
    PageResponse<Attraction> searchAttractions(String city, String keyword, Integer page, Integer pageSize);

    /**
     * 获取景点详情
     * @param id 景点 ID
     * @return 景点信息
     */
    Attraction getAttractionDetail(Long id);

    /**
     * 获取热门景点
     * @param city 城市名称
     * @return 热门景点列表
     */
    List<Attraction> getPopularAttractions(String city);

    /**
     * 获取推荐景点
     * @param limit 限制数量
     * @return 推荐景点列表
     */
    List<Attraction> getRecommendedAttractions(Integer limit);
}
