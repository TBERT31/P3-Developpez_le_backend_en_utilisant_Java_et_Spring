package com.chatop.backend.service;

import com.chatop.backend.dto.RentalDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RentalService {
    List<RentalDTO> getRentals();
    Optional<RentalDTO> getRentalById(Integer rental_id);
    Optional<RentalDTO> createRental(RentalDTO rentalDTO, MultipartFile picture) throws IOException;
    Optional<RentalDTO> updateRental(Integer rental_id, RentalDTO rentalDTO, MultipartFile picture) throws IOException;
    void  deleteRental(Integer rental_id) throws IOException;

}
