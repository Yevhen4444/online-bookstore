package com.example.bookstore.service.impl;

import com.example.bookstore.dto.AddToCartRequestDto;
import com.example.bookstore.dto.ShoppingCartDto;
import com.example.bookstore.dto.UpdateCartItemRequestDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.ShoppingCart;
import com.example.bookstore.entity.User;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.ShoppingCartService;
import com.example.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto getCart() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userService.findByEmail(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(
                        () -> new RuntimeException("Can't find shopping cart for user with email: "
                                + email));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addToCart(AddToCartRequestDto requestDto) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userService.findByEmail(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException(
                        "Can't find shopping cart for user with email: " + email));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new RuntimeException(
                        "Can't find book by id: " + requestDto.getBookId()));

        CartItem existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + requestDto.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
            cartItem.setQuantity(requestDto.getQuantity());
            CartItem savedCartItem = cartItemRepository.save(cartItem);
            shoppingCart.getCartItems().add(savedCartItem);
        }

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userService.findByEmail(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException(
                        "Can't find shopping cart for user with email: " + email));
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException(
                        "Can't find cart item by id: " + cartItemId));
        if (!cartItem.getShoppingCart().getId().equals(shoppingCart.getId())) {
            throw new RuntimeException("Cart item doesn't belong to current user's shopping cart");
        }
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userService.findByEmail(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException(
                        "Can't find shopping cart for user with email: " + email));
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException(
                        "Can't find cart item by id: " + cartItemId));
        if (!cartItem.getShoppingCart().getId().equals(shoppingCart.getId())) {
            throw new RuntimeException("Cart item doesn't belong to current user's shopping cart");
        }

        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }
}
