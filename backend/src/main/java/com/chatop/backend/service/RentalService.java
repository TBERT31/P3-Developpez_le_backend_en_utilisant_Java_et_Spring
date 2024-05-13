package com.chatop.backend.service;

import com.chatop.backend.dto.RentalDTO;
import com.chatop.backend.entity.Rental;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface RentalService {
    List<RentalDTO> getRentals();

    Optional<RentalDTO> getRentalById(Integer rentalId);
}
