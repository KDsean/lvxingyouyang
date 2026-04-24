package com.lvxingyouyang.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String country;

    private String description;

    private String image;

    @Column(name = "popularity_score")
    private Double popularityScore;

    @ElementCollection
    @CollectionTable(name = "destination_tags", joinColumns = @JoinColumn(name = "destination_id"))
    @Column(name = "tag")
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "destination_seasons", joinColumns = @JoinColumn(name = "destination_id"))
    @Column(name = "season")
    private List<String> bestSeason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    private Integer heatCount;

    @Transient
    private Integer heatRank;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
