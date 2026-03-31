package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CreateOrderRequestDto;
import com.example.bookstore.dto.OrderItemResponseDto;
import com.example.bookstore.dto.OrderResponseDto;
import com.example.bookstore.dto.UpdateOrderStatusRequestDto;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.Order;
import com.example.bookstore.entity.OrderItem;
import com.example.bookstore.entity.OrderStatus;
import com.example.bookstore.entity.ShoppingCart;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto dto, User user) {

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(dto.getShippingAddress());

        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUser(user)
                .orElseThrow();

        Set<CartItem> cartItems = shoppingCart.getCartItems();

        Set<OrderItem> orderItems = new HashSet<>();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItems.add(orderItem);
            BigDecimal price = cartItem.getBook().getPrice();
            BigDecimal quantity = new BigDecimal(cartItem.getQuantity());
            BigDecimal itemTotal = price.multiply(quantity);
            totalPrice = totalPrice.add(itemTotal);
        }

        order.setOrderItems(orderItems);
        order.setTotal(totalPrice);
        orderRepository.save(order);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(order.getId());
        responseDto.setUserId(order.getUser().getId());
        responseDto.setOrderDate(order.getOrderDate());
        responseDto.setTotal(order.getTotal());
        responseDto.setStatus(order.getStatus());
        List<OrderItemResponseDto> itemDtos = new ArrayList<>();

        for (OrderItem item : orderItems) {
            OrderItemResponseDto itemDto = new OrderItemResponseDto();
            itemDto.setId(item.getId());
            itemDto.setBookId(item.getBook().getId());
            itemDto.setQuantity(item.getQuantity());

            itemDtos.add(itemDto);
        }
        responseDto.setOrderItems(itemDtos);
        return responseDto;
    }

    @Override
    public List<OrderResponseDto> getOrdersHistory(User user, Pageable pageable) {

        Page<Order> page = orderRepository.findByUser(user, pageable);
        List<Order> orders = page.getContent();
        List<OrderResponseDto> response = new ArrayList<>();

        for (Order order : orders) {
            OrderResponseDto responseDto = new OrderResponseDto();
            responseDto.setId(order.getId());
            responseDto.setUserId(order.getUser().getId());
            responseDto.setOrderDate(order.getOrderDate());
            responseDto.setTotal(order.getTotal());
            responseDto.setStatus(order.getStatus());

            List<OrderItemResponseDto> items = new ArrayList<>();

            for (OrderItem item : order.getOrderItems()) {
                OrderItemResponseDto itemDto = new OrderItemResponseDto();
                itemDto.setId(item.getId());
                itemDto.setBookId(item.getBook().getId());
                itemDto.setQuantity(item.getQuantity());
                items.add(itemDto);
            }
            responseDto.setOrderItems(items);
            response.add(responseDto);
        }
        return response;
    }

    @Override
    public void updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(dto.getStatus());
        orderRepository.save(order);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        Set<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemResponseDto> response = new ArrayList<>();
        for (OrderItem item : orderItems) {
            OrderItemResponseDto itemDto = new OrderItemResponseDto();
            itemDto.setId(item.getId());
            itemDto.setBookId(item.getBook().getId());
            itemDto.setQuantity(item.getQuantity());
            response.add(itemDto);
        }
        return response;
    }

    @Override
    public OrderItemResponseDto getOrderItemById(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        Set<OrderItem> orderItems = order.getOrderItems();

        for (OrderItem item : orderItems) {
            if (item.getId().equals(itemId)) {

                OrderItemResponseDto dto = new OrderItemResponseDto();
                dto.setId(item.getId());
                dto.setBookId(item.getBook().getId());
                dto.setQuantity(item.getQuantity());

                return dto;
            }
        }

        throw new RuntimeException("Item not found");
    }
}
