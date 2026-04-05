package com.example.bookstore.dto;

import com.example.bookstore.entity.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderResponseDto {
    private Long id;
    private OrderStatus status;
}
