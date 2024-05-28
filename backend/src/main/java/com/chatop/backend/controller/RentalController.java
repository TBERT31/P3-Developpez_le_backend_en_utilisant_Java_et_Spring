package com.chatop.backend.controller;

import com.chatop.backend.dto.RentalDTO;
import com.chatop.backend.dto.request.RentalRequest;
import com.chatop.backend.dto.response.MessageResponse;
import com.chatop.backend.dto.response.RentalsListResponse;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.service.RentalService;
import com.chatop.backend.service.UserService;
import com.chatop.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.io.IOException;
import java.time.LocalDateTime;
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
    public ResponseEntity<RentalsListResponse> getRentals(){
        RentalsListResponse rentalsListResponse = new RentalsListResponse();
        rentalsListResponse.setRentals(rentalService.getRentals());

        return ResponseEntity.ok().body(
                rentalsListResponse
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<RentalDTO>> getRentalById(@PathVariable Integer id){
        return ResponseEntity.ok(rentalService.getRentalById(id.longValue()));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createRental(
            @Valid RentalRequest rentalRequest,
            @RequestHeader("Authorization") String token
    ) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);

        Optional<UserDTO> userDTO = userService.getUserByEmail(email);
        MessageResponse messageResponse = new MessageResponse();

        if (userDTO.isPresent()) {
            RentalDTO rentalDTO = RentalDTO.builder()
                    .name(rentalRequest.getName())
                    .surface(rentalRequest.getSurface())
                    .price(rentalRequest.getPrice())
                    .description(rentalRequest.getDescription())
                    .owner_id(userDTO.get().getId())
                    .created_at(LocalDateTime.now())
                    .updated_at(null)
                    .build();

            try {
                Optional<RentalDTO> createdRental = rentalService.createRental(rentalDTO, rentalRequest.getPicture());

                if (createdRental.isPresent()) {
                    messageResponse.setMessage("Rental created!");
                    return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
                } else {
                    messageResponse.setMessage("Failed to create rental");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            messageResponse
                    );
                }
            } catch (IOException e) {
                messageResponse.setMessage("Failed to upload picture");
                System.err.println("Failed to upload picture");
                e.printStackTrace(System.err);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        messageResponse
                );
            }
        } else {
            messageResponse.setMessage("Owner not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    messageResponse
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRental(
            @PathVariable("id") Integer id,
            @Valid RentalRequest rentalRequest,
            @RequestHeader("Authorization") String token
    ) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);

        Optional<UserDTO> userDTO = userService.getUserByEmail(email);
        Optional<RentalDTO> existingRentalDTO = rentalService.getRentalById(id.longValue());
        MessageResponse messageResponse = new MessageResponse();

        if (userDTO.isPresent() && existingRentalDTO.isPresent()) {
            if (userDTO.get().getId().equals(existingRentalDTO.get().getOwner_id())) {
                RentalDTO rentalDTO = RentalDTO.builder()
                        .name(rentalRequest.getName())
                        .surface(rentalRequest.getSurface())
                        .price(rentalRequest.getPrice())
                        .owner_id(userDTO.get().getId())
                        .description(rentalRequest.getDescription())
                        .updated_at(LocalDateTime.now())
                        .build();

                try {
                    Optional<RentalDTO> updatedRental = rentalService.updateRental(id, rentalDTO, rentalRequest.getPicture());
                    if (updatedRental.isPresent()) {
                        messageResponse.setMessage("Rental updated!");
                        return ResponseEntity.ok().body(messageResponse);
                    } else {
                        messageResponse.setMessage("Failed to update rental");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Failed to upload picture");
                    e.printStackTrace(System.err);
                    messageResponse.setMessage("Failed to upload picture");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
                }
            } else {
                messageResponse.setMessage("You are not authorized to update this rental");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageResponse);
            }
        }else{
            messageResponse.setMessage("Resource not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRental(@PathVariable("id") Integer id) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            rentalService.deleteRental(id);
            messageResponse.setMessage("Rental deleted!");
            return ResponseEntity.ok(messageResponse);
        } catch (IOException e) {
            System.err.println("Failed to delete rental");
            e.printStackTrace(System.err);
            messageResponse.setMessage("Failed to delete rental");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }
    }
}
