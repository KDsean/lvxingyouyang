package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Hotel;
import com.lvxingyouyang.repository.HotelRepository;
import com.lvxingyouyang.service.IHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 酒店服务实现类
 * 实现酒店搜索、查询、推荐等功能
 */
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements IHotelService {
    private final HotelRepository hotelRepository;

    /**
     * 搜索酒店
     * 支持按城市或关键词搜索
     */
    @Override
    public PageResponse<Hotel> searchHotels(String city, String keyword, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Hotel> result;

        if (city != null && !city.isEmpty()) {
            // 按城市搜索
            result = hotelRepository.findByCity(city, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            // 按关键词搜索
            result = hotelRepository.findByNameContaining(keyword, pageable);
        } else {
            // 获取所有酒店
            result = hotelRepository.findAll(pageable);
        }

        return PageResponse.<Hotel>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取酒店详情
     */
    @Override
    public Hotel getHotelDetail(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("酒店不存在"));
    }

    /**
     * 获取推荐酒店
     * 按评分排序，返回评分最高的酒店
     */
    @Override
    public List<Hotel> getRecommendedHotels(Integer limit) {
        return hotelRepository.findTop10ByOrderByRatingDesc().stream()
                .limit(limit)
                .toList();
    }

    /**
     * 获取热门酒店
     * 如果指定城市，则返回该城市的热门酒店
     * 否则返回全部热门酒店
     */
    @Override
    public List<Hotel> getPopularHotels(String city) {
        if (city != null && !city.isEmpty()) {
            return hotelRepository.findByCity(city, PageRequest.of(0, 10)).getContent();
        }
        return hotelRepository.findTop10ByOrderByRatingDesc();
    }
}
