package com.Vechile_Service.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdateDto {

    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian number"
    )
    private String customerPhone;

}
