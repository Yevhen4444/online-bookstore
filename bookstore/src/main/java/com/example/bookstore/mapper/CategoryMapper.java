package com.example.bookstore.mapper;

import com.example.bookstore.dto.CategoryDto;
import com.example.bookstore.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);
}
