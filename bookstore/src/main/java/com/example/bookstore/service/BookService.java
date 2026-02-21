package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Page<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto createBook(CreateBookRequestDto bookDto);

    BookDto updateBook(Long id, CreateBookRequestDto bookDto);

    void deleteBook(Long id);

    Page<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable);

}
