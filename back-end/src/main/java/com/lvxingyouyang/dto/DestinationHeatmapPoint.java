package com.lvxingyouyang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationHeatmapPoint {
    private Long id;
    private String destination;
    private String country;
    private Integer count;
    private Double lat;
    private Double lng;
    private Double score;
}
