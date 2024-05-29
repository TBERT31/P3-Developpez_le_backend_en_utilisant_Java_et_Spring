package com.chatop.backend.service;

import com.chatop.backend.entity.Rental;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RentalService {
    List<Rental> getRentals();
    Optional<Rental> getRentalById(Long rental_id);
    Rental createRental(Rental rental, MultipartFile picture) throws IOException;
    Rental updateRental(Long rental_id, Rental rental, MultipartFile picture) throws IOException;
    void  deleteRental(Long rental_id) throws IOException;
}
