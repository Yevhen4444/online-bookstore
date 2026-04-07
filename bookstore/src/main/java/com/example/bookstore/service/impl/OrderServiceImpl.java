package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CreateOrderRequestDto;
import com.example.bookstore.dto.OrderItemResponseDto;
import com.example.bookstore.dto.OrderResponseDto;
import com.example.bookstore.dto.UpdateOrderResponseDto;
import com.example.bookstore.dto.UpdateOrderStatusRequestDto;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.Order;
import com.example.bookstore.entity.OrderItem;
import com.example.bookstore.entity.OrderStatus;
import com.example.bookstore.entity.ShoppingCart;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.EntityNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto dto, User user) {
        Order order = createOrderEntity(dto, user);
        ShoppingCart shoppingCart = getShoppingCartByUser(user);
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Set<OrderItem> orderItems = createOrderItems(cartItems, order);
        BigDecimal totalPrice = calculateTotalPrice(cartItems);
        order.setOrderItems(orderItems);
        order.setTotal(totalPrice);
        Order savedOrder = orderRepository.save(order);
        clearShoppingCart(shoppingCart);
        return mapToOrderResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersHistory(User user, Pageable pageable) {
        return orderRepository.findByUser(user, pageable)
                .map(this::mapToOrderResponseDto);
    }

    @Override
    public UpdateOrderResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + id));
        order.setStatus(dto.getStatus());
        Order savedOrder = orderRepository.save(order);
        return mapToUpdateOrderResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
        return order.getOrderItems().stream()
                .map(this::mapToOrderItemResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto getOrderItemById(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
        return order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .map(this::mapToOrderItemResponseDto)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found with id: " + itemId
                                + " in order with id: " + orderId));
    }

    private Order createOrderEntity(CreateOrderRequestDto dto, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(dto.getShippingAddress());
        return order;
    }

    private ShoppingCart getShoppingCartByUser(User user) {
        return shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user: " + user.getId()));
    }

    private Set<OrderItem> createOrderItems(Set<CartItem> cartItems, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            orderItems.add(mapCartItemToOrderItem(cartItem, order));
        }
        return orderItems;
    }

    private OrderItem mapCartItemToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItem;
    }

    private BigDecimal calculateTotalPrice(Set<CartItem> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            BigDecimal itemPrice = cartItem.getBook().getPrice();
            BigDecimal itemQuantity = BigDecimal.valueOf(cartItem.getQuantity());
            totalPrice = totalPrice.add(itemPrice.multiply(itemQuantity));
        }
        return totalPrice;
    }

    private void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }

    private OrderResponseDto mapToOrderResponseDto(Order order) {
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(order.getId());
        responseDto.setUserId(order.getUser().getId());
        responseDto.setOrderDate(order.getOrderDate());
        responseDto.setTotal(order.getTotal());
        responseDto.setStatus(order.getStatus());
        responseDto.setOrderItems(mapToOrderItemResponseDtoList(order.getOrderItems()));
        return responseDto;
    }

    private List<OrderItemResponseDto> mapToOrderItemResponseDtoList(Set<OrderItem> orderItems) {
        List<OrderItemResponseDto> itemDtos = new ArrayList<>();

        for (OrderItem item : orderItems) {
            itemDtos.add(mapToOrderItemResponseDto(item));
        }
        return itemDtos;
    }

    private OrderItemResponseDto mapToOrderItemResponseDto(OrderItem item) {
        OrderItemResponseDto itemDto = new OrderItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setBookId(item.getBook().getId());
        itemDto.setQuantity(item.getQuantity());
        return itemDto;
    }

    private UpdateOrderResponseDto mapToUpdateOrderResponseDto(Order order) {
        UpdateOrderResponseDto responseDto = new UpdateOrderResponseDto();
        responseDto.setId(order.getId());
        responseDto.setStatus(order.getStatus());
        return responseDto;
    }
}
