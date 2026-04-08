package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @GetMapping
    public ApiResponse<?> getMyOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(orderService.getMyOrders(userId, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getOrderDetail(@PathVariable Long id) {
        return ApiResponse.success(orderService.getOrderDetail(id));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<?> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ApiResponse.success("取消成功");
    }
}
