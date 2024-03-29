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
    List<Book> findAllBooksByTitle(String title);
    Category createCategory(Category category);
    List<Category> getAllCategories();
    Category readCategory(UUID id);
}