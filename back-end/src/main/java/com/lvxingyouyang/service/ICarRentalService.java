package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.CarRental;

/**
 * 租车服务接口
 * 提供租车搜索、查询等功能
 */
public interface ICarRentalService {
    /**
     * 搜索租车
     * @param location 地点
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页租车列表
     */
    PageResponse<CarRental> searchCars(String location, Integer page, Integer pageSize);

    /**
     * 获取租车详情
     * @param id 租车 ID
     * @return 租车信息
     */
    CarRental getCarDetail(Long id);
}
