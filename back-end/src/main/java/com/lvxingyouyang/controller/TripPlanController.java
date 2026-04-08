package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.ITripPlanService;
import com.lvxingyouyang.entity.TripPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripPlanController {
    private final ITripPlanService tripPlanService;

    @GetMapping
    public ApiResponse<?> getMyTrips(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(tripPlanService.getMyTrips(userId, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getTripDetail(@PathVariable Long id) {
        return ApiResponse.success(tripPlanService.getTripDetail(id));
    }

    @PostMapping
    public ApiResponse<?> createTrip(Authentication authentication, @RequestBody TripPlan tripPlan) {
        Long userId = (Long) authentication.getPrincipal();
        tripPlan.setUserId(userId);
        return ApiResponse.success(tripPlanService.createTrip(tripPlan));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateTrip(@PathVariable Long id, @RequestBody TripPlan tripPlan) {
        return ApiResponse.success(tripPlanService.updateTrip(id, tripPlan));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteTrip(@PathVariable Long id) {
        tripPlanService.deleteTrip(id);
        return ApiResponse.success("删除成功");
    }
}
