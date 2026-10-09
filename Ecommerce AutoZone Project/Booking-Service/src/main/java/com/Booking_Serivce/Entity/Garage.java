package com.Booking_Serivce.Entity;

// entity/Garage.java
import com.Booking_Serivce.Constant.EntityStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "GARAGE")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "garage_id", updatable = false, nullable = false)
    private UUID garageId;

    @Column(name = "garage_name", nullable = false, length = 50)
    private String garageName;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "contact_info", length = 50)
    private String contactInfo;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    @Column(name = "weekly_off", length = 20)
    private String weeklyOff;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EntityStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = EntityStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}