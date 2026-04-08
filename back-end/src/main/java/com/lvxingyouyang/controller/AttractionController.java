package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.IAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attractions")
@RequiredArgsConstructor
public class AttractionController {
    private final IAttractionService attractionService;

    @GetMapping("/search")
    public ApiResponse<?> searchAttractions(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(attractionService.searchAttractions(city, keyword, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getAttractionDetail(@PathVariable Long id) {
        return ApiResponse.success(attractionService.getAttractionDetail(id));
    }

    @GetMapping("/popular")
    public ApiResponse<?> getPopularAttractions(@RequestParam(required = false) String city) {
        return ApiResponse.success(attractionService.getPopularAttractions(city));
    }

    @GetMapping("/recommended")
    public ApiResponse<?> getRecommendedAttractions(@RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.success(attractionService.getRecommendedAttractions(limit));
    }

    @PostMapping("/buy-ticket")
    public ApiResponse<?> buyTicket(@RequestBody Object ticketData) {
        return ApiResponse.success("购票成功");
    }
}
