package com.example.bookstore.mapper;

import com.example.bookstore.dto.CategoryDto;
import com.example.bookstore.dto.CreateCategoryRequestDto;
import com.example.bookstore.dto.UpdateCategoryRequestDto;
import com.example.bookstore.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    Category toEntity(CreateCategoryRequestDto requestDto);

    void updateCategoryFromDto(UpdateCategoryRequestDto dto, @MappingTarget Category category);
}
