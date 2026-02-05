package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> getAll() {
        List<Book> books = bookRepository.findAll();
        List<BookDto> bookDtos = books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
        return bookDtos;
    }

    @Override
    public BookDto getBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book book = optionalBook.orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: " + id)
        );
        BookDto bookDto = bookMapper.toDto(book);
        return bookDto;
    }

    @Override
    public BookDto createBook(CreateBookRequestDto dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        BookDto bookDto = bookMapper.toDto(savedBook);
        return bookDto;
    }
}
