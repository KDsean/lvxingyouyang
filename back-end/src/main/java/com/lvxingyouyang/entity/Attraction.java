package com.lvxingyouyang.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "attractions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    private String type;

    private Double rating;

    @Column(nullable = false)
    private Double price;

    @Column(name = "open_time")
    private String openTime;

    private String description;

    @ElementCollection
    @CollectionTable(name = "attraction_images", joinColumns = @JoinColumn(name = "attraction_id"))
    @Column(name = "image_url")
    private List<String> images;

    @ElementCollection
    @CollectionTable(name = "attraction_tags", joinColumns = @JoinColumn(name = "attraction_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
