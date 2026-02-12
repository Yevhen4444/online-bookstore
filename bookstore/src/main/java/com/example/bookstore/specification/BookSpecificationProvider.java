package com.example.bookstore.specification;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationProvider {

    private static final String TITLE_FIELD = "title";
    private static final String AUTHOR_FIELD = "author";
    private static final String LIKE_PATTERN = "%";

    public Specification<Book> getTitleLikeSpecification(String titlePart) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(TITLE_FIELD)),
                        LIKE_PATTERN + titlePart.toLowerCase() + LIKE_PATTERN);
    }

    public Specification<Book> getAuthorLikeSpecification(String author) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(AUTHOR_FIELD)),
                        LIKE_PATTERN + author.toLowerCase() + LIKE_PATTERN);
    }
}
