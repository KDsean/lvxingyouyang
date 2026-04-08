package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.IRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final IRestaurantService restaurantService;

    @GetMapping("/search")
    public ApiResponse<?> searchRestaurants(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Integer priceLevel,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(restaurantService.searchRestaurants(city, keyword, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getRestaurantDetail(@PathVariable Long id) {
        return ApiResponse.success(restaurantService.getRestaurantDetail(id));
    }

    @GetMapping("/popular")
    public ApiResponse<?> getPopularRestaurants(@RequestParam(required = false) String city) {
        return ApiResponse.success(restaurantService.getPopularRestaurants(city));
    }

    @GetMapping("/recommended")
    public ApiResponse<?> getRecommendedRestaurants(@RequestParam(required = false) String city) {
        return ApiResponse.success(restaurantService.getRecommendedRestaurants(city));
    }

    @PostMapping("/book")
    public ApiResponse<?> bookRestaurant(@RequestBody Object bookingData) {
        return ApiResponse.success("预订成功");
    }
}
