package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Hotel;
import java.util.List;

/**
 * 酒店服务接口
 * 提供酒店搜索、查询、推荐等功能
 */
public interface IHotelService {
    /**
     * 搜索酒店
     * @param city 城市名称
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页酒店列表
     */
    PageResponse<Hotel> searchHotels(String city, String keyword, Integer page, Integer pageSize);

    /**
     * 获取酒店详情
     * @param id 酒店 ID
     * @return 酒店信息
     */
    Hotel getHotelDetail(Long id);

    /**
     * 获取推荐酒店
     * @param limit 限制数量
     * @return 推荐酒店列表
     */
    List<Hotel> getRecommendedHotels(Integer limit);

    /**
     * 获取热门酒店
     * @param city 城市名称
     * @return 热门酒店列表
     */
    List<Hotel> getPopularHotels(String city);
}
