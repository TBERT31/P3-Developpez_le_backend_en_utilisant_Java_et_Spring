package com.chatop.backend.controller;

import com.chatop.backend.dto.RentalDTO;
import com.chatop.backend.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rentals")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<RentalDTO>> getRentals(){
        return ResponseEntity.ok(rentalService.getRentals());
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<Optional<RentalDTO>> getRentalById(@PathVariable Integer rentalId){
        return ResponseEntity.ok(rentalService.getRentalById(rentalId));
    }

    @PostMapping
    public ResponseEntity<?> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture
            //@RequestParam("owner_id") Long owner_id
    ) {
        RentalDTO rentalDTO = RentalDTO.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .description(description)
                //.owner_id(1L)
                .created_at(LocalDateTime.now())
                .updated_at(null)
                .build();

        try {
            Optional<RentalDTO> createdRental = rentalService.createRental(rentalDTO, picture);
            if (createdRental.isPresent()) {
                return ResponseEntity.ok().body(Map.of("message", "Rental created!"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to create rental"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to upload picture"));
        }
    }
}