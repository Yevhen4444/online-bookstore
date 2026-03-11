package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Create a new book")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto dto) {
        return bookService.createBook(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update book by id")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody @Valid CreateBookRequestDto dto) {
        return bookService.updateBook(id, dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete book by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Search books by parameters with pagination")
    @GetMapping("/search")
    public Page<BookDto> searchBooks(BookSearchParametersDto params,
                                     @ParameterObject Pageable pageable) {
        return bookService.searchBooks(params, pageable);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get all books with pagination")
    @GetMapping
    public Page<BookDto> getAll(@ParameterObject Pageable pageable) {
        return bookService.getAll(pageable);
    }
}
