package com.example.library.dto.mapper;

import com.example.library.dto.BookDto;
import com.example.library.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "categoryId", source = "category.id")
    BookDto toDto(Book book);
    @Mapping(target = "category.id", source = "categoryId")
    Book toEntity(BookDto bookDto);

    List<BookDto> toDtos(List<Book> books);
}
