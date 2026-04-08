package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 火车数据访问接口
 */
@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    /**
     * 根据出发城市和到达城市查询（原有）
     */
    Page<Train> findByDepartureCityAndArrivalCity(String departureCity, String arrivalCity, Pageable pageable);

    /**
     * 同时支持城市名和站名查询
     * 前端可能传 "北京" 或 "北京南"，两种都能匹配
     */
    @Query("SELECT t FROM Train t WHERE " +
           "(t.departureCity = :from OR t.departureStation = :from) AND " +
           "(t.arrivalCity = :to OR t.arrivalStation = :to)")
    Page<Train> findByFromAndTo(@Param("from") String from, @Param("to") String to, Pageable pageable);
}
