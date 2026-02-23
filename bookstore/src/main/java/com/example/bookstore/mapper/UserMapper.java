package com.example.bookstore.mapper;

import com.example.bookstore.dto.UserRegistrationRequestDto;
import com.example.bookstore.dto.UserResponseDto;
import com.example.bookstore.entity.User;

public interface UserMapper {

    User toModel(UserRegistrationRequestDto dto);

    UserResponseDto toDto(User user);
}
