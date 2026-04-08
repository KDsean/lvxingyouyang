package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ApiResponse<?> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(productService.getProducts(category, keyword, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getProductDetail(@PathVariable Long id) {
        return ApiResponse.success(productService.getProductDetail(id));
    }

    @GetMapping("/popular")
    public ApiResponse<?> getPopularProducts(@RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.success(productService.getPopularProducts(limit));
    }

    @GetMapping("/recommended")
    public ApiResponse<?> getRecommendedProducts(@RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.success(productService.getRecommendedProducts(limit));
    }

    @PostMapping("/cart/add")
    public ApiResponse<?> addToCart(@RequestBody Object cartData) {
        return ApiResponse.success("添加成功");
    }

    @GetMapping("/cart")
    public ApiResponse<?> getCart() {
        return ApiResponse.success("购物车");
    }

    @PostMapping("/orders/create")
    public ApiResponse<?> createOrder(@RequestBody Object orderData) {
        return ApiResponse.success("订单创建成功");
    }
}
