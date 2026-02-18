package com.example.bookstore.specification;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    private static final String TITLE_FIELD = "title";
    private static final String LIKE_PATTERN = "%";

    @Override
    public String getKey() {
        return "titlePart";
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(TITLE_FIELD)),
                        LIKE_PATTERN + param.toLowerCase() + LIKE_PATTERN);
    }
}
