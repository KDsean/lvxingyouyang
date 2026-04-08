package com.lvxingyouyang.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 酒店实体类
 * 对应数据库 hotels 表
 */
@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    /** 酒店 ID，主键自增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 酒店名称，不为空 */
    @Column(nullable = false)
    private String name;

    /** 酒店位置 */
    private String location;

    /** 城市，不为空 */
    @Column(nullable = false)
    private String city;

    /** 详细地址 */
    private String address;

    /** 评分（1-5 星） */
    private Double rating;

    /** 评论数 */
    @Column(name = "review_count")
    private Integer reviewCount;

    /** 价格，不为空 */
    @Column(nullable = false)
    private Double price;

    /** 酒店图片 URL 列表 */
    @ElementCollection
    @CollectionTable(name = "hotel_images", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "image_url")
    private List<String> images;

    /** 酒店描述 */
    private String description;

    /** 酒店设施列表（如：WiFi、停车场等） */
    @ElementCollection
    @CollectionTable(name = "hotel_facilities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "facility")
    private List<String> facilities;

    /** 星级（1-5 星） */
    @Column(name = "star_level")
    private Integer starLevel;

    /** 酒店标签列表（如：豪华、经济等） */
    @ElementCollection
    @CollectionTable(name = "hotel_tags", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "tag")
    private List<String> tags;

    /** 创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 创建前回调
     * 自动设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
