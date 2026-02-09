package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto dto);

    void updateBookFromDto(CreateBookRequestDto dto,
                           @MappingTarget Book book);
}
