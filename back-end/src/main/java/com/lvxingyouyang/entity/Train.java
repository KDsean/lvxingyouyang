package com.lvxingyouyang.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trains")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "train_number", nullable = false)
    private String trainNumber;

    @Column(name = "departure_station")
    private String departureStation;

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "arrival_station")
    private String arrivalStation;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "arrival_time")
    private String arrivalTime;

    private String duration;

    @ElementCollection
    @CollectionTable(name = "train_seat_types", joinColumns = @JoinColumn(name = "train_id"))
    private List<String> seatTypes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
