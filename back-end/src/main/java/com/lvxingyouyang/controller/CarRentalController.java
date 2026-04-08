package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.ICarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarRentalController {
    private final ICarRentalService carRentalService;

    @GetMapping("/search")
    public ApiResponse<?> searchCars(
            @RequestParam String location,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(carRentalService.searchCars(location, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getCarDetail(@PathVariable Long id) {
        return ApiResponse.success(carRentalService.getCarDetail(id));
    }

    @GetMapping("/popular-locations")
    public ApiResponse<?> getPopularLocations() {
        return ApiResponse.success("热门地点");
    }

    @PostMapping("/book")
    public ApiResponse<?> bookCar(@RequestBody Object bookingData) {
        return ApiResponse.success("预订成功");
    }
}
