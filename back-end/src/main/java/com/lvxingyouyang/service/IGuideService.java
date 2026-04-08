package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Guide;
import java.util.List;

/**
 * 攻略服务接口
 * 提供攻略发布、查询、推荐等功能
 */
public interface IGuideService {
    /**
     * 获取攻略列表
     * @param destination 目的地
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页攻略列表
     */
    PageResponse<Guide> getGuides(String destination, Integer page, Integer pageSize);

    /**
     * 获取攻略详情
     * @param id 攻略 ID
     * @return 攻略信息
     */
    Guide getGuideDetail(Long id);

    /**
     * 发布攻略
     * @param guide 攻略信息
     * @return 发布后的攻略
     */
    Guide publishGuide(Guide guide);

    /**
     * 点赞攻略
     * @param id 攻略 ID
     */
    void likeGuide(Long id);

    /**
     * 获取热门攻略
     * @param limit 限制数量
     * @return 热门攻略列表
     */
    List<Guide> getPopularGuides(Integer limit);

    /**
     * 搜索攻略
     * @param keyword 标题关键词
     * @param destination 目的地
     * @param tag 标签
     * @param sortBy 排序方式
     * @param page 页码
     * @param pageSize 每页数量
     * @return 搜索结果列表
     */
    List<Guide> searchGuides(String keyword, String destination, String tag, String sortBy, Integer page, Integer pageSize);
}
