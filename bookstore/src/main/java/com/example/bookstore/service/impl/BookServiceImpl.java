package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import com.example.bookstore.specification.BookSpecificationProvider;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private static final String TITLE_FIELD = "title";
    private static final String AUTHOR_FIELD = "author";
    private static final String IS_DELETED_FIELD = "isDeleted";

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationProvider bookSpecificationProvider;

    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAllByIsDeletedFalse().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto createBook(CreateBookRequestDto dto) {
        Book book = bookMapper.toEntity(dto);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto dto) {
        Book book = bookRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: " + id)
        );

        bookMapper.updateBookFromDto(dto, book);
        bookRepository.save(book);

        return bookMapper.toDto(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookRepository.softDeleteById(id);
    }

    @Override
    public List<BookDto> searchBooks(BookSearchParametersDto params) {

        Specification<Book> specification = Specification.where(
                (root, query, cb) -> cb.isFalse(root.get(IS_DELETED_FIELD))
        );

        if (params.titlePart() != null && !params.titlePart().isBlank()) {
            specification = specification.and(
                    bookSpecificationProvider.getTitleLikeSpecification(params.titlePart())
            );
        }

        if (params.author() != null && !params.author().isBlank()) {
            specification = specification.and(
                    bookSpecificationProvider.getAuthorLikeSpecification(params.author())
            );
        }

        return bookRepository.findAll(specification).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
