package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.specification.BookSpecificationBuilder;
import com.example.bookstore.util.TestDataHelper;
import com.example.bookstore.exception.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void shouldReturnBookById() {
        Long id = 1L;
        Book book = TestDataHelper.createBook(id, "title", "author", "1234567890");
        BookDto bookDto = TestDataHelper.createBookDto(id, "title", "author", "1234567890");
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto result = bookService.getBookById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("title", result.getTitle());
        assertEquals("author", result.getAuthor());
        assertEquals("1234567890", result.getIsbn());
        assertEquals(new BigDecimal("500.00"), result.getPrice());
        assertEquals("Programming book", result.getDescription());
        verify(bookRepository).findById(id);
        verify(bookMapper).toDto(book);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBookById(id));
        assertEquals("Book not found with id: " + id, exception.getMessage());
        verify(bookRepository).findById(id);
        verify(bookMapper, never()).toDto(any(Book.class));
    }

    @Test
    void shouldDeleteBookById() {
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);
        bookService.deleteBook(id);
        verify(bookRepository).existsById(id);
        verify(bookRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingBook() {
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(false);
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.deleteBook(id));
        assertEquals("Book not found with id: " + id, exception.getMessage());
        verify(bookRepository).existsById(id);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldReturnAllBooksByCategoryId() {
        Long id = 1L;
        Long categoryId = 1L;
        Book book = TestDataHelper.createBook(id, "title", "author", "1234567890");
        BookDtoWithoutCategoryIds bookDto =
                TestDataHelper.createBookDtoWithoutCategoryIds(id, "title", "author");
        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);
        List<BookDtoWithoutCategoryIds> result = bookService.findAllByCategoryId(categoryId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals("title", result.get(0).getTitle());
        assertEquals("author", result.get(0).getAuthor());

        verify(bookRepository).findAllByCategoryId(categoryId);
        verify(bookMapper).toDtoWithoutCategories(book);
    }

    @Test
    void shouldCreateBookSuccessfully() {
        CreateBookRequestDto requestDto = TestDataHelper.createBookRequestDto("title", "author");
        requestDto.setCategoryIds(Set.of(1L, 2L));
        Book book = TestDataHelper.createBook("title", "author", "1234567890");
        Category firstCategory = TestDataHelper.createCategory("Programming", "Programming books");
        Category secondCategory = TestDataHelper.createCategory("Software", "Software books");
        BookDto bookDto = TestDataHelper.createBookDto(1L, "title", "author", "1234567890");
        bookDto.setCategoryIds(Set.of(1L, 2L));
        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(firstCategory));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(secondCategory));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto result = bookService.createBook(requestDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("title", result.getTitle());
        assertEquals("author", result.getAuthor());
        assertEquals("1234567890", result.getIsbn());
        assertEquals(new BigDecimal("500.00"), result.getPrice());
        assertEquals(Set.of(1L, 2L), result.getCategoryIds());
        assertNotNull(book.getCategories());
        assertEquals(2, book.getCategories().size());
        assertTrue(book.getCategories().contains(firstCategory));
        assertTrue(book.getCategories().contains(secondCategory));
        verify(bookMapper).toEntity(requestDto);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(2L);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        Long id = 1L;

        CreateBookRequestDto requestDto = TestDataHelper.createBookRequestDto(
                "Effective Java",
                "Joshua Bloch",
                "1234567890",
                new BigDecimal("650.00"),
                "Java best practices",
                "effective-java.jpg",
                Set.of(1L, 2L));
        Book existingBook = TestDataHelper.createBook(
                id,
                "Old Title",
                "Old Author",
                "0987654321");
        existingBook.setPrice(new BigDecimal("400.00"));
        existingBook.setDescription("Old description");
        existingBook.setCoverImage("old.jpg");
        Category firstCategory = TestDataHelper.createCategory(1L, "Programming");
        Category secondCategory = TestDataHelper.createCategory(2L, "Java");
        BookDto bookDto = TestDataHelper.createBookDto(
                id,
                "Effective Java",
                "Joshua Bloch",
                "1234567890",
                new BigDecimal("650.00"),
                "Java best practices",
                "effective-java.jpg",
                Set.of(1L, 2L));
        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(firstCategory));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(secondCategory));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.toDto(existingBook)).thenReturn(bookDto);
        BookDto result = bookService.updateBook(id, requestDto);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Effective Java", result.getTitle());
        assertEquals("Joshua Bloch", result.getAuthor());
        assertEquals("1234567890", result.getIsbn());
        assertEquals(new BigDecimal("650.00"), result.getPrice());
        assertEquals(Set.of(1L, 2L), result.getCategoryIds());
        assertNotNull(existingBook.getCategories());
        assertEquals(2, existingBook.getCategories().size());
        assertTrue(existingBook.getCategories().contains(firstCategory));
        assertTrue(existingBook.getCategories().contains(secondCategory));
        verify(bookRepository).findById(id);
        verify(bookMapper).updateBookFromDto(requestDto, existingBook);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(2L);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toDto(existingBook);
    }

    @Test
    void shouldReturnAllBooks() {
        Long id = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Book book = TestDataHelper.createBook(
                id,
                "Effective Java",
                "Joshua Bloch",
                "1234567890");
        book.setPrice(new BigDecimal("650.00"));
        BookDto bookDto = TestDataHelper.createBookDto(
                id,
                "Effective Java",
                "Joshua Bloch",
                "1234567890",
                new BigDecimal("650.00"),
                "Programming book",
                "cover.jpg",
                Set.of(1L, 2L));
        Page<Book> booksPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        Page<BookDto> result = bookService.getAll(pageable);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(id, result.getContent().get(0).getId());
        assertEquals("Effective Java", result.getContent().get(0).getTitle());
        assertEquals("Joshua Bloch", result.getContent().get(0).getAuthor());
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    void shouldSearchBooksByParameters() {
        Long id = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchParametersDto params = new BookSearchParametersDto("Effective", "Joshua Bloch");
        Book book = TestDataHelper.createBook(id, "Effective Java", "Joshua Bloch", "1234567890");
        book.setPrice(new BigDecimal("650.00"));
        BookDto bookDto = TestDataHelper.createBookDto(
                id,
                "Effective Java",
                "Joshua Bloch",
                "1234567890",
                new BigDecimal("650.00"),
                "Programming book",
                "cover.jpg",
                Set.of(1L, 2L));
        @SuppressWarnings("unchecked")
        Specification<Book> specification =
                (Specification<Book>) org.mockito.Mockito.mock(Specification.class);
        Page<Book> booksPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        Page<BookDto> result = bookService.searchBooks(params, pageable);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(id, result.getContent().get(0).getId());
        assertEquals("Effective Java", result.getContent().get(0).getTitle());
        assertEquals("Joshua Bloch", result.getContent().get(0).getAuthor());
        verify(bookSpecificationBuilder).build(params);
        verify(bookRepository).findAll(specification, pageable);
        verify(bookMapper).toDto(book);
    }
}
