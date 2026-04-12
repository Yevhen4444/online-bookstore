package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldSaveAndFindBookById() {
        Book book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.valueOf(100));

        Book savedBook = bookRepository.save(book);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("title");
        assertThat(foundBook.get().getAuthor()).isEqualTo("author");
    }

    @Test
    void shouldFindAllBooks() {
        Book book1 = new Book();
        book1.setTitle("title");
        book1.setAuthor("author");
        book1.setIsbn("isbn");
        book1.setPrice(BigDecimal.valueOf(100));

        Book book2 = new Book();
        book2.setTitle("title2");
        book2.setAuthor("author2");
        book2.setIsbn("isbn2");
        book2.setPrice(BigDecimal.valueOf(100));

        bookRepository.save(book1);
        bookRepository.save(book2);
        List<Book> books = bookRepository.findAll();

        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(2);
    }

    @Test
    void shouldDeleteBookById() {
        Book book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.valueOf(100));

        Book savedBook = bookRepository.save(book);
        bookRepository.deleteById(savedBook.getId());
        Optional<Book> found = bookRepository.findById(savedBook.getId());
        assertThat(found).isEmpty();
    }
}
