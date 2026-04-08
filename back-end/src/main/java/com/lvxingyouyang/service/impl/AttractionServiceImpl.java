package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Attraction;
import com.lvxingyouyang.repository.AttractionRepository;
import com.lvxingyouyang.service.IAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 景点服务实现类
 * 实现景点搜索、查询、推荐等功能
 */
@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements IAttractionService {
    private final AttractionRepository attractionRepository;

    /**
     * 搜索景点
     * 支持按城市或关键词搜索
     */
    @Override
    public PageResponse<Attraction> searchAttractions(String city, String keyword, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Attraction> result;

        if (city != null && !city.isEmpty()) {
            result = attractionRepository.findByCity(city, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            result = attractionRepository.findByNameContaining(keyword, pageable);
        } else {
            result = attractionRepository.findAll(pageable);
        }

        return PageResponse.<Attraction>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取景点详情
     */
    @Override
    public Attraction getAttractionDetail(Long id) {
        return attractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("景点不存在"));
    }

    /**
     * 获取热门景点
     * 如果指定城市，则返回该城市的热门景点
     */
    @Override
    public List<Attraction> getPopularAttractions(String city) {
        if (city != null && !city.isEmpty()) {
            return attractionRepository.findByCity(city, PageRequest.of(0, 10)).getContent();
        }
        return attractionRepository.findTop10ByOrderByRatingDesc();
    }

    /**
     * 获取推荐景点
     * 按评分排序
     */
    @Override
    public List<Attraction> getRecommendedAttractions(Integer limit) {
        return attractionRepository.findTop10ByOrderByRatingDesc().stream()
                .limit(limit)
                .toList();
    }
}
