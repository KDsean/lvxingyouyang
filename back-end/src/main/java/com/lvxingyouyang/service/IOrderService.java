package com.lvxingyouyang.service;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Order;

/**
 * 订单服务接口
 * 提供订单查询、创建、取消等功能
 */
public interface IOrderService {
    /**
     * 获取我的订单列表
     * @param userId 用户 ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页订单列表
     */
    PageResponse<Order> getMyOrders(Long userId, Integer page, Integer pageSize);

    /**
     * 获取订单详情
     * @param id 订单 ID
     * @return 订单信息
     */
    Order getOrderDetail(Long id);

    /**
     * 创建订单
     * @param order 订单信息
     * @return 创建后的订单
     */
    Order createOrder(Order order);

    /**
     * 取消订单
     * @param id 订单 ID
     */
    void cancelOrder(Long id);
}
