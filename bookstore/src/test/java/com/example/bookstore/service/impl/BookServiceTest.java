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
import jakarta.persistence.EntityNotFoundException;
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
class BookServiceTest {

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

        Book book = new Book();
        book.setId(id);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setIsbn("1234567890");
        book.setPrice(new BigDecimal("500.00"));
        book.setDescription("Programming book");
        book.setCoverImage("cover.jpg");

        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle("Clean Code");
        bookDto.setAuthor("Robert Martin");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(new BigDecimal("500.00"));
        bookDto.setDescription("Programming book");
        bookDto.setCoverImage("cover.jpg");
        bookDto.setCategoryIds(Set.of(1L, 2L));

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getBookById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert Martin", result.getAuthor());
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
                () -> bookService.getBookById(id)
        );

        assertEquals("Book not found with id: " + id, exception.getMessage());

        verify(bookRepository).findById(id);
        verify(bookMapper, never()).toDto(any());
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
                () -> bookService.deleteBook(id)
        );

        assertEquals("Book not found with id: " + id, exception.getMessage());

        verify(bookRepository).existsById(id);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldReturnAllBooksByCategoryId() {
        Long categoryId = 1L;

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setPrice(new BigDecimal("500.00"));

        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(1L);
        bookDto.setTitle("Clean Code");
        bookDto.setAuthor("Robert Martin");
        bookDto.setPrice(500.0);

        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

        List<BookDtoWithoutCategoryIds> result = bookService.findAllByCategoryId(categoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals("Robert Martin", result.get(0).getAuthor());

        verify(bookRepository).findAllByCategoryId(categoryId);
        verify(bookMapper).toDtoWithoutCategories(book);
    }

    @Test
    void shouldCreateBookSuccessfully() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Clean Code");
        requestDto.setAuthor("Robert Martin");
        requestDto.setIsbn("1234567890");
        requestDto.setPrice(new BigDecimal("500.00"));
        requestDto.setDescription("Programming book");
        requestDto.setCoverImage("cover.jpg");
        requestDto.setCategoryIds(Set.of(1L, 2L));

        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setIsbn("1234567890");
        book.setPrice(new BigDecimal("500.00"));
        book.setDescription("Programming book");
        book.setCoverImage("cover.jpg");

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Programming");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Best Practices");

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Clean Code");
        bookDto.setAuthor("Robert Martin");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(new BigDecimal("500.00"));
        bookDto.setDescription("Programming book");
        bookDto.setCoverImage("cover.jpg");
        bookDto.setCategoryIds(Set.of(1L, 2L));

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.createBook(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert Martin", result.getAuthor());
        assertEquals("1234567890", result.getIsbn());
        assertEquals(new BigDecimal("500.00"), result.getPrice());
        assertEquals(Set.of(1L, 2L), result.getCategoryIds());

        assertNotNull(book.getCategories());
        assertEquals(2, book.getCategories().size());
        assertTrue(book.getCategories().contains(category1));
        assertTrue(book.getCategories().contains(category2));

        verify(bookMapper).toEntity(requestDto);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(2L);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        Long id = 1L;

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Effective Java");
        requestDto.setAuthor("Joshua Bloch");
        requestDto.setIsbn("1234567890");
        requestDto.setPrice(new BigDecimal("650.00"));
        requestDto.setDescription("Java best practices");
        requestDto.setCoverImage("effective-java.jpg");
        requestDto.setCategoryIds(Set.of(1L, 2L));

        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");
        existingBook.setIsbn("0987654321");
        existingBook.setPrice(new BigDecimal("400.00"));
        existingBook.setDescription("Old description");
        existingBook.setCoverImage("old.jpg");

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Programming");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Java");

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Effective Java");
        bookDto.setAuthor("Joshua Bloch");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(new BigDecimal("650.00"));
        bookDto.setDescription("Java best practices");
        bookDto.setCoverImage("effective-java.jpg");
        bookDto.setCategoryIds(Set.of(1L, 2L));

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.toDto(existingBook)).thenReturn(bookDto);

        BookDto result = bookService.updateBook(id, requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Effective Java", result.getTitle());
        assertEquals("Joshua Bloch", result.getAuthor());
        assertEquals("1234567890", result.getIsbn());
        assertEquals(new BigDecimal("650.00"), result.getPrice());
        assertEquals(Set.of(1L, 2L), result.getCategoryIds());

        assertNotNull(existingBook.getCategories());
        assertEquals(2, existingBook.getCategories().size());
        assertTrue(existingBook.getCategories().contains(category1));
        assertTrue(existingBook.getCategories().contains(category2));

        verify(bookRepository).findById(id);
        verify(bookMapper).updateBookFromDto(requestDto, existingBook);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(2L);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toDto(existingBook);
    }

    @Test
    void shouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("1234567890");
        book.setPrice(new BigDecimal("650.00"));

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Effective Java");
        bookDto.setAuthor("Joshua Bloch");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(new BigDecimal("650.00"));
        bookDto.setCategoryIds(Set.of(1L, 2L));

        Page<Book> booksPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Effective Java", result.getContent().get(0).getTitle());
        assertEquals("Joshua Bloch", result.getContent().get(0).getAuthor());

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    void shouldSearchBooksByParameters() {
        Pageable pageable = PageRequest.of(0, 10);

        BookSearchParametersDto params = new BookSearchParametersDto("Effective", "Joshua Bloch");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("1234567890");
        book.setPrice(new BigDecimal("650.00"));

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Effective Java");
        bookDto.setAuthor("Joshua Bloch");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(new BigDecimal("650.00"));
        bookDto.setCategoryIds(Set.of(1L, 2L));

        @SuppressWarnings("unchecked")
        Specification<Book> specification = (Specification<Book>) org.mockito.Mockito.mock(Specification.class);
        Page<Book> booksPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.searchBooks(params, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Effective Java", result.getContent().get(0).getTitle());
        assertEquals("Joshua Bloch", result.getContent().get(0).getAuthor());

        verify(bookSpecificationBuilder).build(params);
        verify(bookRepository).findAll(specification, pageable);
        verify(bookMapper).toDto(book);
    }
}