package com.example.bookstore.specification;

import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.entity.Book;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> getBooks(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.titlePart() != null && !params.titlePart().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + params.titlePart().toLowerCase() + "%"
                ));
            }

            if (params.author() != null && !params.author().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("author")),
                        "%" + params.author().toLowerCase() + "%"
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
