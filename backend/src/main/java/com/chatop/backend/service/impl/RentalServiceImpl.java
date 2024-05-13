package com.chatop.backend.service.impl;


import com.chatop.backend.dto.RentalDTO;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;

    private RentalDTO rentalDTO;

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
}
