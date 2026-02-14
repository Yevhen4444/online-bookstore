package com.example.bookstore.specification;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    private static final String AUTHOR_FIELD = "author";
    private static final String LIKE_PATTERN = "%";

    @Override
    public String getKey() {
        return "author";
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(AUTHOR_FIELD)),
                        LIKE_PATTERN + param.toLowerCase() + LIKE_PATTERN);
    }
}
