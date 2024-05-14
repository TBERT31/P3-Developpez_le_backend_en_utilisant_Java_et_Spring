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
                //.owner_id(1l)
                .created_at(LocalDateTime.now())
                .updated_at(null)
                .build();

        if (picture != null && !picture.isEmpty()) {
            try {
                // Ensure the uploads directory exists
                String uploadDir = "uploads";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Génére un nom de fichier unique en utilisant le nom du fichier original et l'horodatage actuel.
                String originalFileName = picture.getOriginalFilename();
                String fileNameWithoutExtension = "";
                String fileExtension = "";
                if (originalFileName != null && originalFileName.contains(".")) {
                    int dotIndex = originalFileName.lastIndexOf("."); // trouve l'index du dernier "." dans le nom du file
                    fileNameWithoutExtension = originalFileName.substring(0, dotIndex); // récupère tout ce qu'il y a avant l'extension
                    fileExtension = originalFileName.substring(dotIndex); // récupère l'extension
                }
                String uniqueFileName = fileNameWithoutExtension + "_" + System.currentTimeMillis() + fileExtension;

                // Enregistre le fichier dans le répertoire uploads
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(picture.getInputStream(), filePath);

                // Définit l'URL de l'image dans le RentalDTO
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(uniqueFileName)
                        .toUriString();
                rentalDTO.setPicture(fileDownloadUri);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        Map.of("message", "Failed to upload picture")
                );
            }
        }

        Optional<RentalDTO> createdRental = rentalService.createRental(rentalDTO);
        if (createdRental.isPresent()) {
            return ResponseEntity.ok().body(
                    Map.of("message", "Rental created!")
            );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "Failed to create rental")
            );
        }
    }
}
