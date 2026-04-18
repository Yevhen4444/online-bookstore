package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import com.example.bookstore.util.TestDataHelper;
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
        Book book = TestDataHelper.createBook("title", "author", "isbn");

        Book savedBook = bookRepository.save(book);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("title");
        assertThat(foundBook.get().getAuthor()).isEqualTo("author");
    }

    @Test
    void shouldFindAllBooks() {
        Book bookFirst = TestDataHelper.createBook("title", "author", "1234567890123");
        Book bookSecond = TestDataHelper.createBook("title", "author", "1234567890125");
        bookRepository.save(bookFirst);
        bookRepository.save(bookSecond);
        List<Book> books = bookRepository.findAll();

        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(2);
    }

    @Test
    void shouldDeleteBookById() {
        Book book = TestDataHelper.createBook("title", "author", "isbn");
        Book savedBook = bookRepository.save(book);
        bookRepository.deleteById(savedBook.getId());
        Optional<Book> found = bookRepository.findById(savedBook.getId());
        assertThat(found).isEmpty();
    }
}
