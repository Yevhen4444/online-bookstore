package com.example.bookstore.controller;

import com.example.bookstore.dto.CreateOrderRequestDto;
import com.example.bookstore.dto.OrderItemResponseDto;
import com.example.bookstore.dto.OrderResponseDto;
import com.example.bookstore.dto.UpdateOrderStatusRequestDto;
import com.example.bookstore.entity.User;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders", description = "Order management endpoints")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order")
    @PostMapping
    public OrderResponseDto createOrder(
            @Valid @RequestBody CreateOrderRequestDto dto,
            @AuthenticationPrincipal User user) {
        return orderService.createOrder(dto, user);
    }

    @Operation(summary = "Get current user's order history")
    @GetMapping
    public List<OrderResponseDto> getOrdersHistory(@AuthenticationPrincipal User user,
                                                   Pageable pageable) {
        return orderService.getOrdersHistory(user, pageable);
    }

    @Operation(summary = "Update order status (ADMIN)")
    @PatchMapping("/{id}")
    public void updateOrderStatus(@PathVariable Long id,
                                  @Valid @RequestBody UpdateOrderStatusRequestDto dto) {
        orderService.updateOrderStatus(id, dto);
    }

    @Operation(summary = "Get all items for a specific order")
    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }

    @Operation(summary = "Get specific item from an order")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getOrderItemById(@PathVariable Long orderId,
                                                 @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }
}
