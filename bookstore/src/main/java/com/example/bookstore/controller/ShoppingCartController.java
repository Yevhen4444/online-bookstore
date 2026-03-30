package com.example.bookstore.controller;

import com.example.bookstore.dto.AddToCartRequestDto;
import com.example.bookstore.dto.ShoppingCartDto;
import com.example.bookstore.dto.UpdateCartItemRequestDto;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart", description = "Retrieve current user's shopping cart")
    @GetMapping
    public ShoppingCartDto getCart() {
        return shoppingCartService.getCart();
    }

    @Operation(summary = "Add book to cart",
            description = "Add a book to current user's shopping cart")
    @PostMapping
    public ShoppingCartDto addToCart(@RequestBody @Valid AddToCartRequestDto requestDto) {
        return shoppingCartService.addToCart(requestDto);
    }

    @Operation(summary = "Update cart item", description = "Update quantity of a cart item")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateCartItem(@PathVariable Long cartItemId,
                                          @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateCartItem(cartItemId, requestDto);
    }

    @Operation(summary = "Delete cart item", description = "Remove item from shopping cart")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
