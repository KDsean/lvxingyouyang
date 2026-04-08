package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.CarRental;
import com.lvxingyouyang.repository.CarRentalRepository;
import com.lvxingyouyang.service.ICarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 租车服务实现类
 * 实现租车搜索、查询等功能
 */
@Service
@RequiredArgsConstructor
public class CarRentalServiceImpl implements ICarRentalService {
    private final CarRentalRepository carRentalRepository;

    /**
     * 搜索租车
     * 根据地点搜索可用的租车
     */
    @Override
    public PageResponse<CarRental> searchCars(String location, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<CarRental> result = carRentalRepository.findByLocation(location, pageable);

        return PageResponse.<CarRental>builder()
                .list(result.getContent())
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取租车详情
     */
    @Override
    public CarRental getCarDetail(Long id) {
        return carRentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("租车不存在"));
    }
}
