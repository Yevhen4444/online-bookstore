package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.CategoryDto;
import com.example.bookstore.dto.CreateCategoryRequestDto;
import com.example.bookstore.dto.UpdateCategoryRequestDto;
import com.example.bookstore.security.JwtUtil;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnCategoryById() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Fantasy");
        categoryDto.setDescription("Fantasy books");

        when(categoryService.getById(1L)).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllCategories() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Fantasy");
        dto.setDescription("Fantasy books");

        Page<CategoryDto> page = new PageImpl<>(List.of(dto));

        when(categoryService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBooksByCategoryId() throws Exception {
        BookDtoWithoutCategoryIds book = new BookDtoWithoutCategoryIds();
        book.setId(1L);
        book.setTitle("Harry Potter");
        book.setAuthor("Rowling");
        book.setPrice(100.0);

        List<BookDtoWithoutCategoryIds> books = List.of(book);

        when(bookService.findAllByCategoryId(1L))
                .thenReturn(books);

        mockMvc.perform(get("/categories/1/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Harry Potter"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateCategory() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Fantasy");
        requestDto.setDescription("Fantasy books");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("Fantasy");
        responseDto.setDescription("Fantasy books");

        when(categoryService.save(any(CreateCategoryRequestDto.class)))
                .thenReturn(responseDto);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCategoryById() throws Exception {
    mockMvc.perform(delete("/categories/1"))
            .andExpect(status().isOk());
    verify(categoryService).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCategory() throws Exception {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto();
        requestDto.setName("Updated Fantasy");
        requestDto.setDescription("Updated fantasy books");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("Updated Fantasy");
        responseDto.setDescription("Updated fantasy books");

        when(categoryService.update(any(Long.class), any(UpdateCategoryRequestDto.class)))
                .thenReturn(responseDto);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Fantasy"));
    }
}
