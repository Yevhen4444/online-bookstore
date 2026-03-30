package com.example.bookstore.mapper;

import com.example.bookstore.dto.CartItemResponseDto;
import com.example.bookstore.dto.ShoppingCartDto;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);
}
