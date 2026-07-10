package com.example.bookstore.repository;

import com.example.bookstore.entity.Role;
import com.example.bookstore.entity.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
