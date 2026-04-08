package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Flight;
import com.lvxingyouyang.repository.FlightRepository;
import com.lvxingyouyang.service.IFlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 机票服务实现类
 * 实现航班搜索、查询、推荐等功能
 */
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements IFlightService {
    private final FlightRepository flightRepository;

    /**
     * 搜索航班
     * 根据出发城市和到达城市搜索航班
     */
    @Override
    public PageResponse<Flight> searchFlights(String from, String to, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Flight> result;
        boolean hasFrom = from != null && !from.isBlank();
        boolean hasTo   = to   != null && !to.isBlank();
        if (hasFrom && hasTo) {
            result = flightRepository.findByDepartureCityAndArrivalCity(from, to, pageable);
        } else {
            result = flightRepository.findAll(pageable);
        }
        return PageResponse.<Flight>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取航班详情
     */
    @Override
    public Flight getFlightDetail(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("航班不存在"));
    }

    /**
     * 获取热门航线
     * 按价格排序，返回价格最低的航班
     */
    @Override
    public List<Flight> getPopularRoutes() {
        return flightRepository.findTop10ByOrderByPriceAsc();
    }
}
