package com.example.bookstore.service;

import com.example.bookstore.dto.AddToCartRequestDto;
import com.example.bookstore.dto.ShoppingCartDto;
import com.example.bookstore.dto.UpdateCartItemRequestDto;
import com.example.bookstore.entity.User;

public interface ShoppingCartService {

    ShoppingCartDto getCart();

    ShoppingCartDto addToCart(AddToCartRequestDto requestDto);

    ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto);

    void deleteCartItem(Long cartItemId);

    void createShoppingCart(User user);
}
