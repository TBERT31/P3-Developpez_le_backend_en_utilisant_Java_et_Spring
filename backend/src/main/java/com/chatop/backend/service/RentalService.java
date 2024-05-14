package com.chatop.backend.service;

import com.chatop.backend.dto.RentalDTO;
import com.chatop.backend.entity.Rental;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface RentalService {

    List<RentalDTO> getRentals();
    Optional<RentalDTO> getRentalById(Integer rentalId);
    Optional<RentalDTO> createRental(RentalDTO rentalDTO, MultipartFile picture) throws IOException;

}
