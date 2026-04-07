package com.example.bookstore.service;

import com.example.bookstore.dto.CreateOrderRequestDto;
import com.example.bookstore.dto.OrderItemResponseDto;
import com.example.bookstore.dto.OrderResponseDto;
import com.example.bookstore.dto.UpdateOrderResponseDto;
import com.example.bookstore.dto.UpdateOrderStatusRequestDto;
import com.example.bookstore.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto createOrder(CreateOrderRequestDto dto, User user);

    Page<OrderResponseDto> getOrdersHistory(User user, Pageable pageable);

    UpdateOrderResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemById(Long orderId, Long itemId);
}
