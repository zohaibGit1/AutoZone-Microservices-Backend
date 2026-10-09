package com.Booking_Serivce.Entity;

// entity/ServiceType.java
import com.Booking_Serivce.Constant.EntityStatus;
import com.Booking_Serivce.Constant.ServiceCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "SERVICE")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "service_id", updatable = false, nullable = false)
    private UUID serviceId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "estimated_duration_minutes", nullable = false)
    private Integer estimatedDurationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private ServiceCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EntityStatus status;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

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