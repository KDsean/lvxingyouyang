package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Destination;
import com.lvxingyouyang.repository.DestinationRepository;
import com.lvxingyouyang.service.IDestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 目的地服务实现类
 * 实现目的地搜索、查询、推荐等功能
 */
@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements IDestinationService {
    private final DestinationRepository destinationRepository;

    /**
     * 获取目的地列表
     * 支持按国家筛选
     */
    @Override
    public PageResponse<Destination> getDestinations(String country, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Destination> result;

        if (country != null && !country.isEmpty()) {
            result = destinationRepository.findByCountry(country, pageable);
        } else {
            result = destinationRepository.findAll(pageable);
        }

        return PageResponse.<Destination>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取目的地详情
     */
    @Override
    public Destination getDestinationDetail(Long id) {
        return destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("目的地不存在"));
    }

    /**
     * 获取热门目的地
     * 按热度排序
     */
    @Override
    public List<Destination> getPopularDestinations(Integer limit) {
        return destinationRepository.findTop10ByOrderByPopularityScoreDesc().stream()
                .limit(limit)
                .toList();
    }

    /**
     * 搜索目的地
     * 支持按关键词和地区筛选
     * region: domestic=国内, asia=亚洲, europe=欧洲, america=美洲, other=其他
     */
    @Override
    public List<Destination> searchDestinations(String keyword, String region) {
        Pageable pageable = PageRequest.of(0, 20);
        
        // 将 region 映射为对应的国家/地区条件
        String countryFilter = null;
        if (region != null && !region.isEmpty()) {
            switch (region) {
                case "domestic" -> countryFilter = "中国";
                case "asia" -> countryFilter = "亚洲";
                case "europe" -> countryFilter = "欧洲";
                case "america" -> countryFilter = "美洲";
                default -> countryFilter = null;
            }
        }
        
        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        
        if (countryFilter != null && hasKeyword) {
            // 有地区且有关键词：按国家和名称组合查询
            // domestic 特殊处理：查 country="中国"
            if ("domestic".equals(region)) {
                return destinationRepository.findByCountryAndNameContaining(countryFilter, keyword, pageable).getContent();
            }
            return destinationRepository.findByNameContaining(keyword, pageable).getContent();
        } else if (countryFilter != null) {
            // 只有地区：按国家查询
            if ("domestic".equals(region)) {
                return destinationRepository.findByCountry(countryFilter, pageable).getContent();
            }
            return destinationRepository.findAll(pageable).getContent();
        } else if (hasKeyword) {
            // 只有关键词：按名称查询
            return destinationRepository.findByNameContaining(keyword, pageable).getContent();
        } else {
            // 无条件：返回热门目的地
            return destinationRepository.findTop10ByOrderByPopularityScoreDesc();
        }
    }
}
