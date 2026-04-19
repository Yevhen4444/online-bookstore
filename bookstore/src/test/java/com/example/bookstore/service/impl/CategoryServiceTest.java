package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CategoryDto;
import com.example.bookstore.dto.CreateCategoryRequestDto;
import com.example.bookstore.dto.UpdateCategoryRequestDto;
import com.example.bookstore.entity.Category;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.util.TestDataHelper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void shouldReturnCategoryById() {
        Long id = 1L;
        Category category = TestDataHelper.createCategory(id, "Fantasy");
        category.setDescription("Fantasy books");

        CategoryDto categoryDto =
                TestDataHelper.createCategoryDto(id, "Fantasy", "Fantasy books");

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(category));

        when(categoryMapper.toDto(category))
        .thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Fantasy", result.getName());
        assertEquals("Fantasy books", result.getDescription());

        verify(categoryRepository).findById(id);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundById() {
        Long id = 1L;
    when(categoryRepository.findById(id))
        .thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(id));
        assertEquals("Category not found for id: " + id, exception.getMessage());
        verify(categoryRepository).findById(id);
        verify(categoryMapper, never()).toDto(any(Category.class));
    }

   @Test
   void shouldSaveCategorySuccessfully() {
       CreateCategoryRequestDto requestDto =
               TestDataHelper.createCategoryRequestDto("History", "History books");

       Category category = TestDataHelper.createCategory("History", "History books");

       Category savedCategory =
               TestDataHelper.createCategory(1L, "History", "History books");

       CategoryDto categoryDto =
               TestDataHelper.createCategoryDto(1L, "History", "History books");

       when(categoryMapper.toEntity(requestDto))
               .thenReturn(category);
       when(categoryRepository.save(category))
               .thenReturn(savedCategory);
       when(categoryMapper.toDto(savedCategory))
               .thenReturn(categoryDto);
       CategoryDto result = categoryService.save(requestDto);

       assertNotNull(result);
       assertEquals(1L, result.getId());
       assertEquals("History", result.getName());
       assertEquals("History books", result.getDescription());

       verify(categoryRepository).save(category);
       verify(categoryMapper).toDto(savedCategory);
       verify(categoryMapper).toEntity(requestDto);
   }

   @Test
   void shouldUpdateCategorySuccessfully() {
       Long id = 1L;
       UpdateCategoryRequestDto requestDto =
               TestDataHelper.createUpdateCategoryRequestDto(
                       "Updated History", "Updated history books");
       Category existingCategory =
               TestDataHelper.createCategory(id, "History", "Old history books");
       CategoryDto categoryDto =
               TestDataHelper.createCategoryDto(
                       id, "Updated History", "Updated history books");

       when(categoryRepository.findById(id))
               .thenReturn(Optional.of(existingCategory));
       when(categoryMapper.toDto(existingCategory))
               .thenReturn(categoryDto);
       when(categoryRepository.save(existingCategory))
               .thenReturn(existingCategory);

       CategoryDto result = categoryService.update(id, requestDto);
       assertNotNull(result);
       assertEquals(1L, result.getId());
       assertEquals("Updated History", result.getName());
       assertEquals("Updated history books", result.getDescription());

       verify(categoryRepository).findById(id);
       verify(categoryMapper).updateCategoryFromDto(requestDto, existingCategory);
       verify(categoryRepository).save(existingCategory);
       verify(categoryMapper).toDto(existingCategory);
   }

   @Test
   void shouldSoftDeleteCategoryById() {
       Long id = 1L;
       Category category = TestDataHelper.createCategory(id, "Fantasy");

       when(categoryRepository.findById(id))
               .thenReturn(Optional.of(category));
       when(categoryRepository.save(category))
               .thenReturn(category);
       categoryService.deleteById(id);

       assertTrue(category.isDeleted());

       verify(categoryRepository).findById(id);
       verify(categoryRepository).save(category);
   }

   @Test
   void shouldThrowExceptionWhenDeletingNonExistingCategory() {
        Long id = 1L;
        when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.deleteById(id));

        assertEquals("Category not found for id: " + id, exception.getMessage());

        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).save(any());
   }
}
