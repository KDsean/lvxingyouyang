package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.TripPlan;
import com.lvxingyouyang.repository.TripPlanRepository;
import com.lvxingyouyang.service.ITripPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 行程服务实现类
 * 实现行程创建、查询、更新、删除等功能
 */
@Service
@RequiredArgsConstructor
public class TripPlanServiceImpl implements ITripPlanService {
    private final TripPlanRepository tripPlanRepository;

    /**
     * 获取我的行程列表
     * 根据用户 ID 查询该用户的所有行程
     */
    @Override
    public PageResponse<TripPlan> getMyTrips(Long userId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<TripPlan> result = tripPlanRepository.findByUserId(userId, pageable);

        return PageResponse.<TripPlan>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取行程详情
     */
    @Override
    public TripPlan getTripDetail(Long id) {
        return tripPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("行程不存在"));
    }

    /**
     * 创建行程
     */
    @Override
    public TripPlan createTrip(TripPlan tripPlan) {
        return tripPlanRepository.save(tripPlan);
    }

    /**
     * 更新行程
     * 支持更新标题、目的地、日期、预算、行程详情等信息
     */
    @Override
    public TripPlan updateTrip(Long id, TripPlan tripPlan) {
        TripPlan existing = tripPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("行程不存在"));
        
        // 只更新非空字段
        if (tripPlan.getTitle() != null) existing.setTitle(tripPlan.getTitle());
        if (tripPlan.getOrigin() != null) existing.setOrigin(tripPlan.getOrigin());
        if (tripPlan.getDestination() != null) existing.setDestination(tripPlan.getDestination());
        if (tripPlan.getDays() != null) existing.setDays(tripPlan.getDays());
        if (tripPlan.getInterests() != null) existing.setInterests(tripPlan.getInterests());
        if (tripPlan.getStartDate() != null) existing.setStartDate(tripPlan.getStartDate());
        if (tripPlan.getEndDate() != null) existing.setEndDate(tripPlan.getEndDate());
        if (tripPlan.getBudget() != null) existing.setBudget(tripPlan.getBudget());
        if (tripPlan.getPlanDetails() != null) existing.setPlanDetails(tripPlan.getPlanDetails());

        return tripPlanRepository.save(existing);
    }

    /**
     * 删除行程
     */
    @Override
    public void deleteTrip(Long id) {
        tripPlanRepository.deleteById(id);
    }
}
