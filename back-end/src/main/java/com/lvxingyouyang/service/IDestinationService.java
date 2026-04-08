package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Destination;
import java.util.List;

/**
 * 目的地服务接口
 * 提供目的地搜索、查询、推荐等功能
 */
public interface IDestinationService {
    /**
     * 获取目的地列表
     * @param country 国家
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页目的地列表
     */
    PageResponse<Destination> getDestinations(String country, Integer page, Integer pageSize);

    /**
     * 获取目的地详情
     * @param id 目的地 ID
     * @return 目的地信息
     */
    Destination getDestinationDetail(Long id);

    /**
     * 获取热门目的地
     * @param limit 限制数量
     * @return 热门目的地列表
     */
    List<Destination> getPopularDestinations(Integer limit);

    /**
     * 搜索目的地
     * @param keyword 关键词
     * @param region 地区（domestic/asia/europe/america/other）
     * @return 搜索结果列表
     */
    List<Destination> searchDestinations(String keyword, String region);
}
