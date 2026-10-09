package com.Vechile_Service.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComplaintResponseDto {

    private UUID complaintId;
    private String complaintDescription;
    private UUID vehicleVisitId;
}
