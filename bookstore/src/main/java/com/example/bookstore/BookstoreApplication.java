package com.example.bookstore;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class BookstoreApplication {

    private final BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Clean Code");
            book.setAuthor("Robert C. Martin");
            book.setIsbn("9780132350284");
            book.setPrice(BigDecimal.valueOf(39.99));
            book.setDescription("A Handbook of Agile Software Craftsmanship");
            book.setCoverImage("cleancode.jpg");

            bookService.save(book);

            System.out.println("Books in DB: " + bookService.findAll().size());
        };
    }
}
