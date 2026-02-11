package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findAllByIsDeletedFalse();

    Optional<Book> findByIdAndIsDeletedFalse(Long id);

    @Modifying
    @Query("UPDATE Book b SET b.isDeleted = true WHERE b.id = :id")
    void softDeleteById(Long id);
}

