package com.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {

    @NotBlank
    @Size(min = 2, max = 100)
    private String title;

    @NotBlank
    @Size(min = 2, max = 100)
    private String author;

    @NotBlank
    @Size(min = 10, max = 20)
    private String isbn;

    @NotNull
    @Positive
    private BigDecimal price;

    @Size(max = 500)
    private String description;

    private String coverImage;
}
