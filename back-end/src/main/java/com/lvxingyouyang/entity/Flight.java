package com.lvxingyouyang.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String airline;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "departure_airport")
    private String departureAirport;

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "arrival_airport")
    private String arrivalAirport;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(nullable = false)
    private Double price;

    @Column(name = "cabin_class")
    private String cabinClass;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
