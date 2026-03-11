package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CategoryDto;
import com.example.bookstore.entity.Category;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.service.CategoryService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();

    }

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Category category = optionalCategory.orElseThrow(() ->
                new RuntimeException("Category not found for id: " + id));
        CategoryDto categoryDto = categoryMapper.toDto(category);
        return categoryDto;
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        CategoryDto dto = categoryMapper.toDto(savedCategory);
        return dto;
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Category category = optionalCategory.orElseThrow(() ->
                new RuntimeException("Category not found for id: " + id));
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Category category = optionalCategory.orElseThrow(() ->
        new RuntimeException("Category not found for id: " + id));
        category.setDeleted(true);
        categoryRepository.save(category);
    }
}
