package com.example.bookstore.specification;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DeletedSpecificationProvider {

    private static final String IS_DELETED_FIELD = "isDeleted";

    public Specification<Book> getSpecification() {
        return (root, query, cb) -> cb.isFalse(root.get(IS_DELETED_FIELD));
    }
}
