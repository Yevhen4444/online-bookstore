package com.example.bookstore.service.impl;

import com.example.bookstore.dto.UserRegistrationRequestDto;
import com.example.bookstore.dto.UserResponseDto;
import com.example.bookstore.entity.ShoppingCart;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.mapper.UserMapper;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException(
                    String.format("User with email %s already exists", request.getEmail()));
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(savedUser);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toDto(savedUser);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found by email: " + email));
    }
}
