package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.service.BookService;
import com.example.bookstore.specification.BookSpecificationBuilder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto createBook(CreateBookRequestDto dto) {
        Book book = bookMapper.toEntity(dto);
        Set<Category> categories = dto.getCategoryIds().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(()
                                -> new EntityNotFoundException("Category not found: " + id)))
                .collect(Collectors.toSet());
        book.setCategories(categories);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto dto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: " + id));
        bookMapper.updateBookFromDto(dto, book);
        Set<Category> categories = dto.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Category not found with id: "
                                        + categoryId)))
                .collect(Collectors.toSet());
        book.setCategories(categories);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }

        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> searchBooks(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> specification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(specification, pageable)
                .map(bookMapper::toDto);

    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        List<Book> books = bookRepository.findAllByCategoryId(categoryId);
        List<BookDtoWithoutCategoryIds> result = books.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
        return result;
    }
}
