package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CreateOrderRequestDto;
import com.example.bookstore.dto.OrderItemResponseDto;
import com.example.bookstore.dto.OrderResponseDto;
import com.example.bookstore.dto.UpdateOrderStatusRequestDto;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
   private final OrderRepository  orderRepository;
   private final ShoppingCartRepository shoppingCartRepository;
   private final OrderItemRepository orderItemRepository;

   @Override
   public OrderResponseDto createOrder(CreateOrderRequestDto dto) {
      return null;
   }

   @Override
   public List<OrderResponseDto> getOrdersHistory() {
      return List.of();
   }

   @Override
   public void updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto) {

   }

   @Override
   public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
      return List.of();
   }

   @Override
   public OrderItemResponseDto getOrderItemById(Long orderId, Long itemId) {
      return null;
   }
}
