package com.example.bookstore.dto;

import com.example.bookstore.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {

    @NotNull
    private OrderStatus status;
}
