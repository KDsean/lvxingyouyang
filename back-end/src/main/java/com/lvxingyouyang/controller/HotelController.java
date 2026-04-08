package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.IHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 酒店控制器
 * 处理酒店搜索、查询、推荐等请求
 */
@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final IHotelService hotelService;

    /**
     * 搜索酒店
     * @param destination 目的地（城市）
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页酒店列表
     */
    @GetMapping("/search")
    public ApiResponse<?> searchHotels(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(hotelService.searchHotels(destination, keyword, page, pageSize));
    }

    /**
     * 获取酒店详情
     * @param id 酒店 ID
     * @return 酒店信息
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getHotelDetail(@PathVariable Long id) {
        return ApiResponse.success(hotelService.getHotelDetail(id));
    }

    /**
     * 获取推荐酒店
     * @param limit 限制数量
     * @return 推荐酒店列表
     */
    @GetMapping("/recommended")
    public ApiResponse<?> getRecommendedHotels(@RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.success(hotelService.getRecommendedHotels(limit));
    }

    /**
     * 获取热门酒店
     * @param city 城市名称
     * @return 热门酒店列表
     */
    @GetMapping("/popular")
    public ApiResponse<?> getPopularHotels(@RequestParam(required = false) String city) {
        return ApiResponse.success(hotelService.getPopularHotels(city));
    }

    /**
     * 预订酒店
     * @param bookingData 预订数据
     * @return 预订成功消息
     */
    @PostMapping("/book")
    public ApiResponse<?> bookHotel(@RequestBody Object bookingData) {
        return ApiResponse.success("预订成功");
    }
}
