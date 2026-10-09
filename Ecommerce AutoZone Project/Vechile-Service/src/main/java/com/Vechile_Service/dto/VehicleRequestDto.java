package com.Vechile_Service.dto;

import com.Vechile_Service.constant.VehicleType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestDto {

    @NotBlank(message = "Vehicle number is required")
    @Pattern(
            regexp = "^[A-Za-z]{2}\\d{2}[A-Za-z]{1,2}\\d{4}$",
            message = "Invalid vehicle number format"
    )
    private String vehicleNumber;

    @NotBlank(message = "Vehicle name is required")
    @Size(min = 2, max = 50, message = "Vehicle name must be between 2 and 50 characters")
    private String vehicleName;

    @NotBlank(message = "Vehicle model is required")
    @Size(min = 2, max = 50, message = "Vehicle model must be between 2 and 50 characters")
    private String vehicleModel;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

}
