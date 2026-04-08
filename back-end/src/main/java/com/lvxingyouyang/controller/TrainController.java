package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.ITrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trains")
@RequiredArgsConstructor
public class TrainController {
    private final ITrainService trainService;

    /**
     * 搜索火车票
     * GET /api/trains/search?from=北京南&to=上海虹桥&date=2026-05-01&page=1&pageSize=10
     * from/to 支持城市名（北京）或站名（北京南）
     */
    @GetMapping("/search")
    public ApiResponse<?> searchTrains(
            @RequestParam(required = false, defaultValue = "") String from,
            @RequestParam(required = false, defaultValue = "") String to,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return ApiResponse.success(trainService.searchTrains(from, to, page, pageSize));
    }

    /**
     * 获取火车详情
     * GET /api/trains/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getTrainDetail(@PathVariable Long id) {
        return ApiResponse.success(trainService.getTrainDetail(id));
    }

    /**
     * 获取车站列表（供前端自动补全使用）
     * GET /api/trains/stations?keyword=北京
     */
    @GetMapping("/stations")
    public ApiResponse<?> getStations(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(trainService.getStations(keyword));
    }

    /**
     * 预订火车票
     * POST /api/trains/book
     */
    @PostMapping("/book")
    public ApiResponse<?> bookTrain(@RequestBody Object bookingData) {
        return ApiResponse.success("预订成功");
    }
}
