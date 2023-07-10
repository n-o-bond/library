package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Category;

import java.util.List;
import java.util.UUID;

public interface BookService {

    Book create(Book book);
    Book read(UUID id);
    Book update(Book book);
    void delete(UUID id);
    List<Book> findAllByTitle(String title);
    Category create(Category category);
    Category findByName(String name);
}
