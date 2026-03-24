package com.example.bookstore.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @Min(1)
    private int quantity;
}
