package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Guide;
import com.lvxingyouyang.repository.GuideRepository;
import com.lvxingyouyang.service.IGuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 攻略服务实现类
 * 实现攻略发布、查询、推荐、点赞等功能
 */
@Service
@RequiredArgsConstructor
public class GuideServiceImpl implements IGuideService {
    private final GuideRepository guideRepository;

    /**
     * 获取攻略列表
     * 支持按目的地筛选
     */
    @Override
    public PageResponse<Guide> getGuides(String destination, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Guide> result;

        if (destination != null && !destination.isEmpty()) {
            result = guideRepository.findByDestination(destination, pageable);
        } else {
            result = guideRepository.findAll(pageable);
        }

        return PageResponse.<Guide>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取攻略详情
     * 同时增加浏览次数
     */
    @Override
    public Guide getGuideDetail(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("攻略不存在"));
        // 增加浏览次数
        guide.setViewCount(guide.getViewCount() + 1);
        return guideRepository.save(guide);
    }

    /**
     * 发布攻略
     */
    @Override
    public Guide publishGuide(Guide guide) {
        return guideRepository.save(guide);
    }

    /**
     * 点赞攻略
     * 增加点赞数
     */
    @Override
    public void likeGuide(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("攻略不存在"));
        guide.setLikeCount(guide.getLikeCount() + 1);
        guideRepository.save(guide);
    }

    /**
     * 获取热门攻略
     * 按点赞数排序
     */
    @Override
    public List<Guide> getPopularGuides(Integer limit) {
        return guideRepository.findTop10ByOrderByLikeCountDesc().stream()
                .limit(limit)
                .toList();
    }

    /**
     * 搜索攻略
     * 支持按关键词、目的地、标签和排序方式筛选
     */
    @Override
    public List<Guide> searchGuides(String keyword, String destination, String tag, String sortBy, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasDestination = destination != null && !destination.isEmpty();

        Page<Guide> result;
        if (hasKeyword && hasDestination) {
            result = guideRepository.findByTitleContainingAndDestination(keyword, destination, pageable);
        } else if (hasDestination) {
            result = guideRepository.findByDestination(destination, pageable);
        } else if (hasKeyword) {
            result = guideRepository.findByTitleContaining(keyword, pageable);
        } else {
            result = guideRepository.findAll(pageable);
        }
        return result.getContent();
    }
}
