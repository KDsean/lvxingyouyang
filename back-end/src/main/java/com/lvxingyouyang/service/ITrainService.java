package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import java.util.Map;

/**
 * 火车服务接口
 */
public interface ITrainService {
    /**
     * 搜索火车票（返回前端所需嵌套结构）
     * @param from 出发城市或站名
     * @param to   到达城市或站名
     */
    PageResponse<Map<String, Object>> searchTrains(String from, String to, Integer page, Integer pageSize);

    /**
     * 获取火车详情（返回前端所需嵌套结构）
     */
    Map<String, Object> getTrainDetail(Long id);

    /**
     * 获取车站列表（供前端自动补全）
     */
    java.util.List<Map<String, Object>> getStations(String keyword);
}
