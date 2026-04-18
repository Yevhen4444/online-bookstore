package com.example.bookstore.util;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.CategoryDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.dto.CreateCategoryRequestDto;
import com.example.bookstore.dto.UpdateCategoryRequestDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import java.math.BigDecimal;
import java.util.Set;

public class TestDataHelper {

    public static Category createCategory() {
        Category category = new Category();
        category.setName("Default Category");
        category.setDescription("Default Description");
        return category;
    }

    public static Book createBook() {
        Book book = new Book();
        book.setTitle("Default Title");
        book.setAuthor("Default Author");
        book.setIsbn("1234567890123");
        book.setPrice(BigDecimal.valueOf(100));
        book.setDescription("Default Description");
        book.setCoverImage("image.jpg");
        return book;
    }

    public static Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return category;
    }

    public static Book createBook(String title, String author, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(BigDecimal.valueOf(100));
        book.setDescription("Default Description");
        book.setCoverImage("image.jpg");
        return book;
    }

    public static CreateCategoryRequestDto createCategoryRequestDto(
            String name, String description) {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(name);
        requestDto.setDescription(description);
        return requestDto;
    }

    public static UpdateCategoryRequestDto createUpdateCategoryRequestDto(
            String name, String description) {
        UpdateCategoryRequestDto dto = new UpdateCategoryRequestDto();
        dto.setName(name);
        dto.setDescription(description);
        return dto;
    }

    public static CreateBookRequestDto createBookRequestDto(String title, String author) {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Harry Potter");
        requestDto.setAuthor("Rowling");
        requestDto.setIsbn("1234267890");
        requestDto.setPrice(BigDecimal.valueOf(100));
        requestDto.setDescription("Fantasy book");
        requestDto.setCoverImage("image.jpg");
        return requestDto;
    }

    public static BookDto createBookDto(
            Long id, String title, String author, String isbn) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        bookDto.setIsbn(isbn);
        bookDto.setPrice(new BigDecimal("500.00"));
        bookDto.setDescription("Programming book");
        bookDto.setCoverImage("cover.jpg");
        bookDto.setCategoryIds(Set.of(1L, 2L));
        return bookDto;
    }

    public static Book createBook(Long id, String title, String author, String isbn) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(new BigDecimal("500.00"));
        book.setDescription("Programming book");
        book.setCoverImage("cover.jpg");
        return book;
    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds(
            Long id, String title, String author) {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setId(id);
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setPrice(500.0);
        return dto;
    }

    public static CreateBookRequestDto createBookRequestDto(
            String title,
            String author,
            String isbn,
            BigDecimal price,
            String description,
            String coverImage,
            Set<Long> categoryIds
    ) {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setIsbn(isbn);
        dto.setPrice(price);
        dto.setDescription(description);
        dto.setCoverImage(coverImage);
        dto.setCategoryIds(categoryIds);
        return dto;
    }

    public static BookDto createBookDto(
            Long id,
            String title,
            String author,
            String isbn,
            BigDecimal price,
            String description,
            String coverImage,
            Set<Long> categoryIds
    ) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setIsbn(isbn);
        dto.setPrice(price);
        dto.setDescription(description);
        dto.setCoverImage(coverImage);
        dto.setCategoryIds(categoryIds);
        return dto;
    }

    public static Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    public static CategoryDto createCategoryDto(Long id, String name, String description) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(id);
        categoryDto.setName(name);
        categoryDto.setDescription(description);
        return categoryDto;
    }

    public static Category createCategory(Long id, String name, String description) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        return category;
    }
}
