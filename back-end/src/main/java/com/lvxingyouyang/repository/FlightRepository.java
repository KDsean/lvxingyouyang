package com.lvxingyouyang.repository;

import com.lvxingyouyang.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 航班数据访问接口
 * 提供航班相关的数据库操作
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    /**
     * 根据出发城市和到达城市查询航班（分页）
     * @param departureCity 出发城市
     * @param arrivalCity 到达城市
     * @param pageable 分页信息
     * @return 分页航班列表
     */
    Page<Flight> findByDepartureCityAndArrivalCity(String departureCity, String arrivalCity, Pageable pageable);

    /**
     * 获取价格最低的前 10 个航班
     * @return 航班列表
     */
    List<Flight> findTop10ByOrderByPriceAsc();
}
