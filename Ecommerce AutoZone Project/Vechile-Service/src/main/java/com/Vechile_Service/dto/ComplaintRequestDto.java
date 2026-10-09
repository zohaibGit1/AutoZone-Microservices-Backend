package com.Vechile_Service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComplaintRequestDto {

    @NotBlank(message = "Complaint description is required")
    @Size(max = 500, message = "Complaint description must not exceed 500 characters")
    private String complaintDescription;

    @NotNull(message = "Vehicle visit ID is required")
    private UUID vehicleVisitId;
}
