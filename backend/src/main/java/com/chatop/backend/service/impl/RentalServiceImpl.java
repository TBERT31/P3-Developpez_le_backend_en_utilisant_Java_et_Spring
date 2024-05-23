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
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;


    // Définir la taille maximale du fichier (par exemple, 8 Mo)
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
    public Optional<RentalDTO> getRentalById(Long rental_id) {
        Optional<Rental> rental = rentalRepository.findById(rental_id);
        return rental.map(RentalDTO::fromEntity);
    }

    @Override
    public Optional<RentalDTO> createRental(RentalDTO rentalDTO, MultipartFile picture) throws IOException {

        String pictureUrl = processRentalPicture(picture, null);

        Rental rental = RentalDTO.toEntity(rentalDTO);
        rental.setPicture(pictureUrl); // Set the picture URL after processing

        Rental savedRental = rentalRepository.save(rental);
        return Optional.ofNullable(RentalDTO.fromEntity(savedRental));
    }

    @Override
    public Optional<RentalDTO> updateRental(Integer rental_id, RentalDTO rentalDTO, MultipartFile picture) throws IOException {
        // Récupérer l'entité existante
        Optional<Rental> existingRentalOpt = rentalRepository.findById(rental_id.longValue());
        if (existingRentalOpt.isEmpty()) {
            throw new IOException("The rental you are trying to modify does not exist");
        }
        Rental existingRental = existingRentalOpt.get();

        // Mettre à jour les champs de l'entité avec les valeurs de rentalDTO
        existingRental.setName(rentalDTO.getName());
        existingRental.setSurface(rentalDTO.getSurface());
        existingRental.setPrice(rentalDTO.getPrice());
        existingRental.setDescription(rentalDTO.getDescription());
        existingRental.setUpdated_at(LocalDateTime.now());

        // Traitement de l'image (voir fonction dédiée)
        if(picture != null && !picture.isEmpty()) {
            String pictureUrl = processRentalPicture(picture, existingRental);
            existingRental.setPicture(pictureUrl);
        }

        // Enregistrer les modifications dans la base de données
        Rental savedRental = rentalRepository.save(existingRental);
        return Optional.ofNullable(RentalDTO.fromEntity(savedRental));
    }

    @Override
    public void deleteRental(Integer rental_id) throws IOException {
        // Récupérer l'entité existante
        Optional<Rental> existingRentalOpt = rentalRepository.findById(rental_id.longValue());
        if (existingRentalOpt.isEmpty()) {
            throw new IOException("The rental you are trying to delete does not exist");
        }
        Rental existingRental = existingRentalOpt.get();

        // Supprimer l'image associée si elle existe
        String currentPicturePath = existingRental.getPicture();
        if (currentPicturePath != null && !currentPicturePath.isEmpty()) {
            try {
                // Extraire le nom de fichier de l'URL
                String fileName = Paths.get(new URI(currentPicturePath).getPath()).getFileName().toString();
                // Construire le chemin complet en utilisant le répertoire d'uploads
                Path oldFilePath = Paths.get("uploads").resolve(fileName);
                Files.deleteIfExists(oldFilePath);
            } catch (Exception e) {
                throw new IOException("Failed to delete image file", e);
            }
        }

        // Supprimer l'entité du rental de la base de données
        rentalRepository.delete(existingRental);
    }

    private String processRentalPicture(MultipartFile picture, Rental existingRental) throws IOException {
        if (picture != null && !picture.isEmpty()) {
            // Vérifier la taille du fichier
            if (picture.getSize() > MAX_FILE_SIZE) {
                throw new IOException("File size exceeds the maximum allowed size of 8 MB");
            }

            // Vérifier l'extension du fichier
            String originalFileName = picture.getOriginalFilename();
            if (originalFileName != null) {
                // Remplacer les caractères gênants dans les urls
                originalFileName = originalFileName.replaceAll("[ '&]", "_");
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

                // Supprimer l'ancienne image si elle existe
                if(existingRental != null){
                    String currentPicturePath = existingRental.getPicture();
                    if (currentPicturePath != null && !currentPicturePath.isEmpty()) {
                        try {
                            // Extraire le nom de fichier de l'URL
                            String fileName = Paths.get(new URI(currentPicturePath).getPath()).getFileName().toString();
                            // Construire le chemin complet en utilisant le répertoire d'uploads
                            Path oldFilePath = Paths.get("uploads").resolve(fileName);
                            System.out.println(oldFilePath);
                            Files.deleteIfExists(oldFilePath);
                        } catch (Exception e) {
                            throw new IOException("Failed to delete old image file", e);
                        }
                    }
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
                return ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(uniqueFileName)
                        .toUriString();
            }
        }
        return null;
    }
}
