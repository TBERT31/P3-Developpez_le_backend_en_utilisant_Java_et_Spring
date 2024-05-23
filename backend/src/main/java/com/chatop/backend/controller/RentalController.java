package com.chatop.backend.controller;

import com.chatop.backend.dto.RentalDTO;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.service.RentalService;
import com.chatop.backend.service.UserService;
import com.chatop.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity< Map<String,List<RentalDTO>> > getRentals(){
        return ResponseEntity.ok().body(
                Map.of("rentals", rentalService.getRentals())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<RentalDTO>> getRentalById(@PathVariable Integer id){
        return ResponseEntity.ok(rentalService.getRentalById(id.longValue()));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestHeader("Authorization") String token
    ) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);

        Optional<UserDTO> userDTO = userService.getUserByEmail(email);

        if (userDTO.isPresent()) {
            RentalDTO rentalDTO = RentalDTO.builder()
                    .name(name)
                    .surface(surface)
                    .price(price)
                    .description(description)
                    .owner_id(userDTO.get().getId())
                    .created_at(LocalDateTime.now())
                    .updated_at(null)
                    .build();

            try {
                Optional<RentalDTO> createdRental = rentalService.createRental(rentalDTO, picture);
                if (createdRental.isPresent()) {
                    return ResponseEntity.ok().body(
                            Map.of("message", "Rental created!")
                    );
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            Map.of("message", "Failed to create rental")
                    );
                }
            } catch (IOException e) {
                System.err.println("Failed to upload picture");
                e.printStackTrace(System.err);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        Map.of("message", "Failed to upload picture")
                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", "Owner not found")
            );
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRental(
            @PathVariable("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestHeader("Authorization") String token
    ) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);

        Optional<UserDTO> userDTO = userService.getUserByEmail(email);
        Optional<RentalDTO> existingRentalDTO = rentalService.getRentalById(id.longValue());

        if (userDTO.isPresent() && existingRentalDTO.isPresent()) {
            if (userDTO.get().getId().equals(existingRentalDTO.get().getOwner_id())) {
                RentalDTO rentalDTO = RentalDTO.builder()
                        .name(name)
                        .surface(surface)
                        .price(price)
                        .owner_id(userDTO.get().getId())
                        .description(description)
                        .updated_at(LocalDateTime.now())
                        .build();

                try {
                    Optional<RentalDTO> updatedRental = rentalService.updateRental(id, rentalDTO, picture);
                    if (updatedRental.isPresent()) {
                        return ResponseEntity.ok().body(
                                Map.of("message", "Rental updated!")
                        );
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                Map.of("message", "Failed to update rental")
                        );
                    }
                } catch (IOException e) {
                    System.err.println("Failed to upload picture");
                    e.printStackTrace(System.err);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            Map.of("message", "Failed to upload picture")
                    );
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        Map.of("message", "You are not authorized to update this rental")
                );
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", "Resource not found")
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRental(@PathVariable("id") Integer id) {
        try {
            rentalService.deleteRental(id);
            return ResponseEntity.ok(
                    Map.of("message", "Rental deleted!")
            );
        } catch (IOException e) {
            System.err.println("Failed to delete rental");
            e.printStackTrace(System.err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "Failed to delete rental")
            );
        }
    }
}
