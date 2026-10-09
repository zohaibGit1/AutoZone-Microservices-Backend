package com.Vechile_Service.repo;

import com.Vechile_Service.entity.VehicleVisit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleVisitRepository extends JpaRepository<VehicleVisit, UUID> {
    @EntityGraph(attributePaths = {"complaints", "vehicle"})
    List<VehicleVisit> findByVehicleCustomerCustomerId(UUID customerId);

    List<VehicleVisit> findByVehicleVehicleId(UUID vehicleId);
}
