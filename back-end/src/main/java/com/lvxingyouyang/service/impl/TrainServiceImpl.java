package com.lvxingyouyang.service.impl;

import com.lvxingyouyang.dto.PageResponse;
import com.lvxingyouyang.entity.Train;
import com.lvxingyouyang.repository.TrainRepository;
import com.lvxingyouyang.service.ITrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 火车服务实现类
 */
@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements ITrainService {
    private final TrainRepository trainRepository;

    /**
     * 搜索火车票，返回前端所需的嵌套结构
     * 前端 Train 类型要求：
     *   departure: { station, city, time }
     *   arrival:   { station, city, time }
     *   seatTypes: [{ type, price, available }]
     */
    @Override
    public PageResponse<Map<String, Object>> searchTrains(String from, String to, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        boolean hasFrom = from != null && !from.isBlank();
        boolean hasTo   = to   != null && !to.isBlank();
        Page<Train> result;
        if (hasFrom && hasTo) {
            result = trainRepository.findByFromAndTo(from, to, pageable);
        } else {
            result = trainRepository.findAll(pageable);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Train t : result.getContent()) {
            list.add(toDto(t));
        }
        return PageResponse.<Map<String, Object>>builder()
                .list(list)
                .total(result.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 获取火车详情，同样返回嵌套结构
     */
    @Override
    public Map<String, Object> getTrainDetail(Long id) {
        Train t = trainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("火车不存在"));
        return toDto(t);
    }

    /**
     * Entity → 前端 DTO 转换
     * seatTypes 数据库只存字符串，价格和余票按类型给默认值
     */
    private Map<String, Object> toDto(Train t) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", t.getId());
        dto.put("trainNumber", t.getTrainNumber());
        dto.put("duration", t.getDuration());

        // 出发信息
        Map<String, Object> dep = new LinkedHashMap<>();
        dep.put("station", t.getDepartureStation());
        dep.put("city",    t.getDepartureCity());
        dep.put("time",    t.getDepartureTime());
        dto.put("departure", dep);

        // 到达信息
        Map<String, Object> arr = new LinkedHashMap<>();
        arr.put("station", t.getArrivalStation());
        arr.put("city",    t.getArrivalCity());
        arr.put("time",    t.getArrivalTime());
        dto.put("arrival", arr);

        // 座位类型：数据库只有名称，补充默认价格和余票
        List<Map<String, Object>> seatTypes = new ArrayList<>();
        for (String seatName : Optional.ofNullable(t.getSeatTypes()).orElse(Collections.emptyList())) {
            Map<String, Object> seat = new LinkedHashMap<>();
            seat.put("type",      seatName);
            seat.put("price",     getDefaultPrice(t.getTrainNumber(), seatName));
            seat.put("available", getDefaultAvailable(seatName));
            seatTypes.add(seat);
        }
        dto.put("seatTypes", seatTypes);

        return dto;
    }

    /**
     * 根据车次类型和座位类型给出参考价格
     */
    private double getDefaultPrice(String trainNumber, String seatType) {
        boolean isHighSpeed = trainNumber != null &&
                (trainNumber.startsWith("G") || trainNumber.startsWith("C"));
        return switch (seatType) {
            case "商务座"   -> isHighSpeed ? 1800.0 : 500.0;
            case "一等座"   -> isHighSpeed ? 650.0  : 300.0;
            case "二等座"   -> isHighSpeed ? 380.0  : 180.0;
            case "软卧",
                 "高级软卧" -> 500.0;
            case "硬卧"    -> 280.0;
            case "软座"    -> 180.0;
            case "硬座"    -> 120.0;
            case "无座"    -> 100.0;
            default        -> 200.0;
        };
    }

    /**
     * 根据座位类型给出默认余票数
     */
    private int getDefaultAvailable(String seatType) {
        return switch (seatType) {
            case "商务座"           -> 3;
            case "一等座", "软卧",
                 "高级软卧"         -> 12;
            case "无座"             -> 0;
            default                 -> 50;
        };
    }

    /**
     * 获取车站列表（供前端自动补全）
     */
    @Override
    public List<Map<String, Object>> getStations(String keyword) {
        List<Train> all = trainRepository.findAll();
        Set<String> seen = new LinkedHashSet<>();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Train t : all) {
            for (String[] pair : new String[][]{
                    {t.getDepartureStation(), t.getDepartureCity()},
                    {t.getArrivalStation(),   t.getArrivalCity()}
            }) {
                String station = pair[0];
                String city    = pair[1];
                if (station == null) continue;
                if (keyword != null && !keyword.isBlank() &&
                        !station.contains(keyword) &&
                        (city == null || !city.contains(keyword))) continue;
                if (seen.add(station)) {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("value", station);
                    item.put("city",  city);
                    result.add(item);
                }
            }
        }
        return result;
    }
}

