package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Order;
import com.lvxingyouyang.repository.OrderRepository;
import com.lvxingyouyang.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 订单服务实现类
 * 实现订单查询、创建、取消等功能
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;

    /**
     * 获取我的订单列表
     * 根据用户 ID 查询该用户的所有订单
     */
    @Override
    public PageResponse<Order> getMyOrders(Long userId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Order> result = orderRepository.findByUserId(userId, pageable);

        return PageResponse.<Order>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取订单详情
     */
    @Override
    public Order getOrderDetail(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    /**
     * 创建订单
     */
    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    /**
     * 取消订单
     * 将订单状态改为 cancelled
     */
    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setStatus("cancelled");
        orderRepository.save(order);
    }
}
