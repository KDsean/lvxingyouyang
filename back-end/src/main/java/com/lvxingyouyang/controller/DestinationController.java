package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.IDestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {
    private final IDestinationService destinationService;

    @GetMapping
    public ApiResponse<?> getDestinations(
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(destinationService.getDestinations(country, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getDestinationDetail(@PathVariable Long id) {
        return ApiResponse.success(destinationService.getDestinationDetail(id));
    }

    @GetMapping("/popular")
    public ApiResponse<?> getPopularDestinations(@RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.success(destinationService.getPopularDestinations(limit));
    }

    @GetMapping("/search")
    public ApiResponse<?> searchDestinations(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String region) {
        return ApiResponse.success(destinationService.searchDestinations(keyword, region));
    }

    @GetMapping("/heatmap")
    public ApiResponse<?> getDestinationHeatmap() {
        return ApiResponse.success(destinationService.getDestinationHeatmap());
    }
}
