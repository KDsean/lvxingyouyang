package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.TripPlan;

/**
 * 行程服务接口
 * 提供行程创建、查询、更新、删除等功能
 */
public interface ITripPlanService {
    /**
     * 获取我的行程列表
     * @param userId 用户 ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页行程列表
     */
    PageResponse<TripPlan> getMyTrips(Long userId, Integer page, Integer pageSize);

    /**
     * 获取行程详情
     * @param id 行程 ID
     * @return 行程信息
     */
    TripPlan getTripDetail(Long id);

    /**
     * 创建行程
     * @param tripPlan 行程信息
     * @return 创建后的行程
     */
    TripPlan createTrip(TripPlan tripPlan);

    /**
     * 更新行程
     * @param id 行程 ID
     * @param tripPlan 行程信息
     * @return 更新后的行程
     */
    TripPlan updateTrip(Long id, TripPlan tripPlan);

    /**
     * 删除行程
     * @param id 行程 ID
     */
    void deleteTrip(Long id);
}
