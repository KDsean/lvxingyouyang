package com.lvxingyouyang.controller;

import com.lvxingyouyang.dto.ApiResponse;
import com.lvxingyouyang.service.IFlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {
    private final IFlightService flightService;

    @GetMapping("/search")
    public ApiResponse<?> searchFlights(
            @RequestParam(required = false, defaultValue = "") String from,
            @RequestParam(required = false, defaultValue = "") String to,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return ApiResponse.success(flightService.searchFlights(from, to, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getFlightDetail(@PathVariable Long id) {
        return ApiResponse.success(flightService.getFlightDetail(id));
    }

    @GetMapping("/popular-routes")
    public ApiResponse<?> getPopularRoutes() {
        return ApiResponse.success(flightService.getPopularRoutes());
    }

    @PostMapping("/book")
    public ApiResponse<?> bookFlight(@RequestBody Object bookingData) {
        return ApiResponse.success("预订成功");
    }
}
