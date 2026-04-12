package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.security.JwtUtil;
import com.example.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBookById() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Harry Potter");
        bookDto.setAuthor("Rowling");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(BigDecimal.valueOf(100));
        bookDto.setDescription("Fantasy book");
        bookDto.setCoverImage("image.jpg");
        bookDto.setCategoryIds(Set.of(1L, 2L));

        when(bookService.getBookById(1L)).thenReturn(bookDto);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.author").value("Rowling"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllBooks() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Harry Potter");
        bookDto.setAuthor("Rowling");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(BigDecimal.valueOf(100));
        bookDto.setDescription("Fantasy book");
        bookDto.setCoverImage("image.jpg");
        bookDto.setCategoryIds(Set.of(1L, 2L));

        Page<BookDto> page = new PageImpl<>(List.of(bookDto));
        when(bookService.getAll(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Harry Potter"))
                .andExpect(jsonPath("$.content[0].author").value("Rowling"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteBookById() throws Exception {
        mockMvc.perform(delete("/books/1").with(csrf()))                .andExpect(status().isNoContent());
        verify(bookService).deleteBook(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Harry Potter");
        requestDto.setAuthor("Rowling");
        requestDto.setIsbn("1234567890");
        requestDto.setPrice(BigDecimal.valueOf(100));
        requestDto.setDescription("Fantasy book");
        requestDto.setCoverImage("image.jpg");
        requestDto.setCategoryIds(Set.of(1L, 2L));

        BookDto responseDto = new BookDto();
        responseDto.setId(1L);
        responseDto.setTitle("Harry Potter");
        responseDto.setAuthor("Rowling");
        responseDto.setIsbn("1234567890");
        responseDto.setPrice(BigDecimal.valueOf(100));
        responseDto.setDescription("Fantasy book");
        responseDto.setCoverImage("image.jpg");
        responseDto.setCategoryIds(Set.of(1L, 2L));

        when(bookService.createBook(any(CreateBookRequestDto.class)))
                .thenReturn(responseDto);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books").with(csrf())                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.author").value("Rowling"));
    }
}