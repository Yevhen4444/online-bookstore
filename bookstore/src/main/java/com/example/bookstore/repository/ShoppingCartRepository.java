package com.example.bookstore.repository;

import com.example.bookstore.entity.ShoppingCart;
import com.example.bookstore.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);
}
