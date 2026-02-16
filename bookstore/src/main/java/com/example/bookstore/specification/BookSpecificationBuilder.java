package com.example.bookstore.specification;

import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder {

    private final TitleSpecificationProvider titleProvider;
    private final AuthorSpecificationProvider authorProvider;

    public Specification<Book> build(BookSearchParametersDto params) {

        Specification<Book> spec = Specification.where(null);

        if (params.titlePart() != null && !params.titlePart().isBlank()) {
            spec = spec.and(titleProvider.getSpecification(params.titlePart()));
        }

        if (params.author() != null && !params.author().isBlank()) {
            spec = spec.and(authorProvider.getSpecification(params.author()));
        }

        return spec;
    }
}
