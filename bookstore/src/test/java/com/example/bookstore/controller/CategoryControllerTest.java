package com.example.bookstore.controller;

import com.example.bookstore.dto.CreateCategoryRequestDto;
import com.example.bookstore.dto.UpdateCategoryRequestDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.util.TestDataHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository  bookRepository;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnCategoryById() throws Exception {
        categoryRepository.deleteAll();
       Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category savedCategory = categoryRepository.save(category);
        mockMvc.perform(get("/categories/{id}", savedCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllCategories() throws Exception {
       categoryRepository.deleteAll();
        Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category savedCategory = categoryRepository.save(category);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.content[0].name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBooksByCategoryId() throws Exception {
        categoryRepository.deleteAll();
        bookRepository.deleteAll();
        Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category savedCategory = categoryRepository.save(category);
        Book book = TestDataHelper.createBook("Harry Potter", "Rowling", "1234567899999");
        book.setCategories(Set.of(savedCategory));
        Book savedBook = bookRepository.save(book);
        mockMvc.perform(get("/categories/{id}/books", savedCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(savedBook.getId()))
                .andExpect(jsonPath("$[0].title").value("Harry Potter"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateCategory() throws Exception {
        categoryRepository.deleteAll();
        CreateCategoryRequestDto requestDto = TestDataHelper.createCategoryRequestDto("Fantasy", "Fantasy books");
        String json = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCategoryById() throws Exception {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
        Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category savedCategory = categoryRepository.save(category);
        mockMvc.perform(delete("/categories/{id}", savedCategory.getId()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCategory() throws Exception {
        categoryRepository.deleteAll();
        UpdateCategoryRequestDto requestDto = TestDataHelper.createUpdateCategoryRequestDto("Updated Fantasy", "Updated Fantasy books");
        Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category savedCategory =  categoryRepository.save(category);
        String json = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(put("/categories/{id}", savedCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value("Updated Fantasy"));

    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnNotFoundWhenCategoryDoesNotExist() throws Exception {
        mockMvc.perform(get("/categories/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserCreatesCategory() throws Exception {
        categoryRepository.deleteAll();

        CreateCategoryRequestDto requestDto =
                TestDataHelper.createCategoryRequestDto("Fantasy", "Fantasy books");

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
