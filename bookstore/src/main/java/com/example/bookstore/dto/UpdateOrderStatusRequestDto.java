package com.example.bookstore.dto;

import com.example.bookstore.entity.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    private OrderStatus status;



}
