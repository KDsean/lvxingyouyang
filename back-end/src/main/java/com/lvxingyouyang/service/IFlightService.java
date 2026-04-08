package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Flight;
import java.util.List;

/**
 * 机票服务接口
 * 提供航班搜索、查询、推荐等功能
 */
public interface IFlightService {
    /**
     * 搜索航班
     * @param from 出发城市
     * @param to 到达城市
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页航班列表
     */
    PageResponse<Flight> searchFlights(String from, String to, Integer page, Integer pageSize);

    /**
     * 获取航班详情
     * @param id 航班 ID
     * @return 航班信息
     */
    Flight getFlightDetail(Long id);

    /**
     * 获取热门航线
     * @return 热门航线列表
     */
    List<Flight> getPopularRoutes();
}
