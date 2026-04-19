package com.example.bookstore.controller;

import com.example.bookstore.dto.CreateBookRequestDto;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBookById() throws Exception {
        bookRepository.deleteAll();
        Book book = TestDataHelper.createBook("title", "author", "1234562890123");
        Book savedBook = bookRepository.save(book);
        mockMvc.perform(get("/books/{id}", savedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedBook.getId()))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.author").value("author"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllBooks() throws Exception {
        bookRepository.deleteAll();
        Book book = TestDataHelper.createBook("title", "author", "1233567890123");
        Book savedBook = bookRepository.save(book);
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(savedBook.getId()))
                .andExpect(jsonPath("$.content[0].title").value("title"))
                .andExpect(jsonPath("$.content[0].author").value("author"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteBookById() throws Exception {
        bookRepository.deleteAll();
        Book book = TestDataHelper.createBook("title", "author", "1244567890123");
        Book savedBook = bookRepository.save(book);
        mockMvc.perform(delete("/books/{id}", savedBook.getId()).with(csrf()))
                .andExpect(status().isNoContent());
        assertFalse(bookRepository.findById(savedBook.getId()).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateBook() throws Exception {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
        Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category savedCategory = categoryRepository.save(category);
        CreateBookRequestDto requestDto = TestDataHelper.createBookRequestDto("Harry Potter", "Rowling");
        requestDto.setCategoryIds(Set.of(savedCategory.getId()));
        String json = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/books").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.author").value("Rowling"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        mockMvc.perform(get("/books/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserDeletesBook() throws Exception {
        bookRepository.deleteAll();

        Book book = TestDataHelper.createBook("title", "author", "1234577890999");
        Book savedBook = bookRepository.save(book);

        mockMvc.perform(delete("/books/{id}", savedBook.getId()).with(csrf()))
                .andExpect(status().isForbidden());
    }
}
