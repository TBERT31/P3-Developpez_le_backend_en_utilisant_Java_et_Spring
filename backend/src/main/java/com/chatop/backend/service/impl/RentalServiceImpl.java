package com.chatop.backend.service.impl;


import com.chatop.backend.dto.RentalDTO;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.service.RentalService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;

    private RentalDTO rentalDTO;

    // Définir la taille maximale du fichier (par exemple, 5 Mo)
    private static final long MAX_FILE_SIZE = 8 * 1024 * 1024;

    // Liste des extensions de fichiers autorisées
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp", ".avif", ".apng", ".gif", ".svg");

    @Override
    public List<RentalDTO> getRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                .map(RentalDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RentalDTO> getRentalById(Integer rentalId) {
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        return rental.map(RentalDTO::fromEntity);
    }

    @Override
    public Optional<RentalDTO> createRental(RentalDTO rentalDTO, MultipartFile picture) throws IOException {

        if (picture != null && !picture.isEmpty()) {

            // Vérifier la taille du fichier
            if (picture.getSize() > MAX_FILE_SIZE) {
                throw new IOException("File size exceeds the maximum allowed size of 8 MB");
            }

            // Vérifier l'extension du fichier
            String originalFileName = picture.getOriginalFilename();
            if (originalFileName != null) {
                // Remplacer les caractères génants dans les urls
                originalFileName = originalFileName.replace(" ", "_");
                originalFileName = originalFileName.replaceAll("[éèêë]", "e");
                originalFileName = originalFileName.replaceAll("[àâä]", "a");
                originalFileName = originalFileName.replaceAll("[ôö]", "o");
                originalFileName = originalFileName.replace("ç", "c");
                originalFileName = originalFileName.replaceAll("[ùûü]", "u");
                originalFileName = originalFileName.replaceAll("[ïî]", "i");
                originalFileName = originalFileName.replace("ÿ", "y");

                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
                if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                    throw new IOException("File type not allowed. Allowed types are: " + ALLOWED_EXTENSIONS);
                }

                // Assurer que le répertoire d'uploads existe
                String uploadDir = "uploads";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Générer un nom de fichier unique en utilisant le nom du fichier original et l'horodatage actuel
                String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf("."));
                String uniqueFileName = fileNameWithoutExtension + "_" + System.currentTimeMillis() + fileExtension;

                // Enregistrer le fichier dans le répertoire d'uploads
                Path filePath = uploadPath.resolve(uniqueFileName);

                // Compresser et redimensionner l'image en utilisant la librairie Thumbnailator
                try (InputStream inputStream = picture.getInputStream()) {
                    Thumbnails.of(inputStream)
                            .size(960, 639) // taille de l'img std donnée par les développeur front
                            .outputFormat(fileExtension.substring(1)) // enlever le point
                            .outputQuality(0.8)
                            .toFile(filePath.toFile());
                }

                // Définir l'URL de téléchargement du fichier dans le RentalDTO
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(uniqueFileName)
                        .toUriString();
                rentalDTO.setPicture(fileDownloadUri);
            }
        }

        Rental rental = RentalDTO.toEntity(rentalDTO);
        Rental savedRental = rentalRepository.save(rental);
        return Optional.ofNullable(RentalDTO.fromEntity(savedRental));
    }


}
