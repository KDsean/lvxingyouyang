package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Restaurant;
import com.lvxingyouyang.repository.RestaurantRepository;
import com.lvxingyouyang.service.IRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 餐厅服务实现类
 * 实现餐厅搜索、查询、推荐等功能
 */
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements IRestaurantService {
    private final RestaurantRepository restaurantRepository;

    /**
     * 搜索餐厅
     * 支持按城市或关键词搜索
     */
    @Override
    public PageResponse<Restaurant> searchRestaurants(String city, String keyword, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Restaurant> result;

        if (city != null && !city.isEmpty()) {
            result = restaurantRepository.findByCity(city, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            result = restaurantRepository.findByNameContaining(keyword, pageable);
        } else {
            result = restaurantRepository.findAll(pageable);
        }

        return PageResponse.<Restaurant>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取餐厅详情
     */
    @Override
    public Restaurant getRestaurantDetail(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("餐厅不存在"));
    }

    /**
     * 获取热门餐厅
     * 如果指定城市，则返回该城市的热门餐厅
     */
    @Override
    public List<Restaurant> getPopularRestaurants(String city) {
        if (city != null && !city.isEmpty()) {
            return restaurantRepository.findByCity(city, PageRequest.of(0, 10)).getContent();
        }
        return restaurantRepository.findTop10ByOrderByRatingDesc();
    }

    /**
     * 获取推荐餐厅
     * 与热门餐厅相同
     */
    @Override
    public List<Restaurant> getRecommendedRestaurants(String city) {
        return getPopularRestaurants(city);
    }
}
