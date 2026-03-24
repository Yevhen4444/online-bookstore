package com.example.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequestDto {

    @NotNull
    private Long bookId;

    @Min(1)
    private int quantity;
}
