package com.Vechile_Service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "complaint_id", updatable = false, nullable = false)
    private UUID complaintId;

    @Column(name = "complaint_description", nullable = false, length = 500)
    private String complaintDescription;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_visit_id", nullable = false)
    private VehicleVisit vehicleVisit;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
