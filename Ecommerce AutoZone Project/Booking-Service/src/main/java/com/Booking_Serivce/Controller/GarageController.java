package com.Booking_Serivce.Controller;

import com.Booking_Serivce.Dto.GarageRequestDto;
import com.Booking_Serivce.Dto.GarageResponseDto;
import com.Booking_Serivce.Dto.GarageUpdateDto;
import com.Booking_Serivce.Service.GarageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garages")
public class GarageController {

    private final GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping({"", "/create"})
    public ResponseEntity<GarageResponseDto> createGarage(@Valid @RequestBody GarageRequestDto garageRequestDto) {
        GarageResponseDto garageResponseDto = garageService.createGarage(garageRequestDto);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(garageResponseDto);
    }

    @PatchMapping("/{garageId}")
    public ResponseEntity<GarageResponseDto> updateGarage(
            @PathVariable String garageId,
            @Valid @RequestBody GarageUpdateDto garageUpdateDto
    ) {
        GarageResponseDto garageResponseDto = garageService.updateGarage(garageId, garageUpdateDto);
        return ResponseEntity.ok(garageResponseDto);
    }

    @GetMapping("/{garageId}")
    public ResponseEntity<GarageResponseDto> getGarage(
            @PathVariable String garageId
    ) {
        GarageResponseDto garageResponseDto = garageService.getGarageById(garageId);
        return ResponseEntity.ok(garageResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<GarageResponseDto>> listAllGarages() {
        return ResponseEntity.ok(garageService.getAllGarages());
    }


}
