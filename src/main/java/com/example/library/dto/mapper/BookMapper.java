package com.example.library.dto.mapper;

import com.example.library.dto.BookDto;
import com.example.library.model.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);
    Book toEntity(BookDto bookDto);
    List<BookDto> toDtos(List<Book> books);
}
