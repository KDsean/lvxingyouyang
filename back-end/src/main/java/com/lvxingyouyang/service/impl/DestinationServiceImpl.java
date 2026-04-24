package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.DestinationHeatmapPoint;
import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Destination;
import com.lvxingyouyang.repository.DestinationRepository;
import com.lvxingyouyang.service.IDestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 目的地服务实现类
 * 实现目的地搜索、查询、推荐等功能
 */
@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements IDestinationService {
    private final DestinationRepository destinationRepository;
    private static final int DEFAULT_POPULAR_LIMIT = 10;
    private static final double HEAT_MULTIPLIER = 180.0;
    private static final double[] DEFAULT_COORDINATES = {35.8617, 104.1954};
    private static final Map<String, double[]> DESTINATION_COORDINATES = Map.ofEntries(
            Map.entry("北京", new double[]{39.9042, 116.4074}),
            Map.entry("上海", new double[]{31.2304, 121.4737}),
            Map.entry("杭州", new double[]{30.2741, 120.1551}),
            Map.entry("成都", new double[]{30.5728, 104.0668}),
            Map.entry("西安", new double[]{34.3416, 108.9398}),
            Map.entry("大理", new double[]{25.6075, 100.2676}),
            Map.entry("丽江", new double[]{26.8550, 100.2278}),
            Map.entry("三亚", new double[]{18.2528, 109.5119}),
            Map.entry("重庆", new double[]{29.5630, 106.5516}),
            Map.entry("厦门", new double[]{24.4798, 118.0894}),
            Map.entry("广州", new double[]{23.1291, 113.2644}),
            Map.entry("深圳", new double[]{22.5431, 114.0579})
    );

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
        List<GroupedDestination> groupedDestinations = getGroupedDestinationsByHeat();
        int safeLimit = Optional.ofNullable(limit)
                .filter(value -> value > 0)
                .orElse(DEFAULT_POPULAR_LIMIT);
        int resultSize = Math.min(safeLimit, groupedDestinations.size());

        return IntStream.range(0, resultSize)
                .mapToObj(index -> buildPopularDestination(groupedDestinations.get(index), index + 1))
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

    @Override
    public List<DestinationHeatmapPoint> getDestinationHeatmap() {
        return getGroupedDestinationsByHeat().stream()
                .map(this::buildHeatmapPoint)
                .toList();
    }

    private List<GroupedDestination> getGroupedDestinationsByHeat() {
        return destinationRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(DestinationServiceImpl::getPopularityScoreSafe).reversed())
                .collect(Collectors.groupingBy(this::resolveDestinationKey, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> buildGroupedDestination(entry.getKey(), entry.getValue()))
                .sorted(Comparator
                        .comparingInt(GroupedDestination::heatCount)
                        .reversed()
                        .thenComparing(GroupedDestination::destinationKey))
                .toList();
    }

    private GroupedDestination buildGroupedDestination(String destinationKey, List<Destination> groupedDestinations) {
        Destination representative = groupedDestinations.get(0);
        double totalPopularity = groupedDestinations.stream()
                .mapToDouble(DestinationServiceImpl::getPopularityScoreSafe)
                .sum();

        return new GroupedDestination(
                destinationKey,
                representative,
                buildHeatCount(totalPopularity)
        );
    }

    private Destination buildPopularDestination(GroupedDestination groupedDestination, int rank) {
        Destination representative = groupedDestination.representative();
        return Destination.builder()
                .id(representative.getId())
                .name(groupedDestination.destinationKey())
                .country(Optional.ofNullable(representative.getCountry()).orElse("中国"))
                .description(representative.getDescription())
                .image(representative.getImage())
                .popularityScore(representative.getPopularityScore())
                .tags(representative.getTags())
                .bestSeason(representative.getBestSeason())
                .createdAt(representative.getCreatedAt())
                .heatCount(groupedDestination.heatCount())
                .heatRank(rank)
                .build();
    }

    private DestinationHeatmapPoint buildHeatmapPoint(GroupedDestination groupedDestination) {
        Destination representative = groupedDestination.representative();
        double[] coordinates = DESTINATION_COORDINATES.getOrDefault(groupedDestination.destinationKey(), DEFAULT_COORDINATES);

        return DestinationHeatmapPoint.builder()
                .id(representative.getId())
                .destination(groupedDestination.destinationKey())
                .country(Optional.ofNullable(representative.getCountry()).orElse("中国"))
                .count(groupedDestination.heatCount())
                .lat(coordinates[0])
                .lng(coordinates[1])
                .score(getPopularityScoreSafe(representative))
                .build();
    }

    private int buildHeatCount(double totalPopularity) {
        return (int) Math.round(totalPopularity * HEAT_MULTIPLIER);
    }

    private String resolveDestinationKey(Destination destination) {
        String name = Optional.ofNullable(destination.getName()).orElse("未知目的地");
        int separatorIndex = name.indexOf('·');
        if (separatorIndex > 0) {
            return name.substring(0, separatorIndex);
        }
        return name;
    }

    private static double getPopularityScoreSafe(Destination destination) {
        return Optional.ofNullable(destination.getPopularityScore()).orElse(5.0);
    }

    private static final class GroupedDestination {
        private final String destinationKey;
        private final Destination representative;
        private final int heatCount;

        private GroupedDestination(String destinationKey, Destination representative, int heatCount) {
            this.destinationKey = destinationKey;
            this.representative = representative;
            this.heatCount = heatCount;
        }

        private String destinationKey() {
            return destinationKey;
        }

        private Destination representative() {
            return representative;
        }

        private int heatCount() {
            return heatCount;
        }
    }
}
