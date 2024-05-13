package com.chatop.backend.controller;

import com.chatop.backend.dto.RentalDTO;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rentals")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @GetMapping("/")
    public ResponseEntity<List<RentalDTO>> getRentals(){
        return ResponseEntity.ok(rentalService.getRentals());
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<Optional<RentalDTO>> getRentalById(@PathVariable Integer rentalId){
        return ResponseEntity.ok(rentalService.getRentalById(rentalId));
    }
}
