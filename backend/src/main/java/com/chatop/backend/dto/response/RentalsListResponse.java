package com.chatop.backend.dto.response;

import com.chatop.backend.dto.RentalDTO;
import lombok.Data;

import java.util.List;

@Data
public class RentalsListResponse {
    private List<RentalDTO> rentals;
}
