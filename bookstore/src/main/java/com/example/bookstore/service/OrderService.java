package com.example.bookstore.service;

import com.example.bookstore.dto.CreateOrderRequestDto;
import com.example.bookstore.dto.OrderItemResponseDto;
import com.example.bookstore.dto.OrderResponseDto;
import com.example.bookstore.dto.UpdateOrderStatusRequestDto;
import com.example.bookstore.entity.Order;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(CreateOrderRequestDto dto);

    List<OrderResponseDto> getOrdersHistory();

    void updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemById(Long orderId, Long itemId);
}
