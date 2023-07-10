package com.example.library.service.impl;

import com.example.library.exception.NullEntityReferenceException;
import com.example.library.exception.UnacceptableParameterValueException;
import com.example.library.model.Book;
import com.example.library.model.Category;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String NULL_BOOK_MESSAGE = "Book cannot be 'null'";
    private static final String NOT_FOUND_BOOK_MESSAGE = "Book (id=UUID: %s) was not found";
    private static final String BOOK_DELETED_MESSAGE = "Book (id=UUID: %s) was deleted";
    private static final String NOT_FOUND_CATEGORY_MESSAGE = "Category (name=%s) was not found";

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Book create(Book book) {
        checkIfBookIsNull(book);
        return bookRepository.save(book);
    }

    private static void checkIfBookIsNull(Book book) {
        if (book == null){
            log.error(NULL_BOOK_MESSAGE);
            throw new NullEntityReferenceException(NULL_BOOK_MESSAGE);
        }
    }

    @Override
    public Book read(UUID id) {
        return bookRepository.findById(id).orElseThrow(() -> {
                log.error(NOT_FOUND_BOOK_MESSAGE.formatted(id));
                throw new EntityNotFoundException(NOT_FOUND_BOOK_MESSAGE.formatted(id));
        });
    }

    @Override
    public Book update(Book book) {
        checkIfBookIsNull(book);
        read(book.getId());
        return bookRepository.save(book);
    }

    @Override
    public void delete(UUID id) {
        bookRepository.findById(id).ifPresentOrElse(book -> {
            bookRepository.delete(book);
            log.info(BOOK_DELETED_MESSAGE.formatted(id));
        }, () -> {
            log.error(NOT_FOUND_BOOK_MESSAGE.formatted(id));
            throw new EntityNotFoundException(NOT_FOUND_BOOK_MESSAGE.formatted(id));
        });
    }

    @Override
    public List<Book> findAllByTitle(String title) {
        if (title.isBlank()){
            log.error("Title is null or empty!");
            throw new UnacceptableParameterValueException("Title is null or empty!");
        }
        return bookRepository.findAllByTitle(title);
    }

    @Override
    public Category create(Category category) {
        if (category == null){
            log.error("Category cannot be 'null'");
            throw new NullEntityReferenceException("Category cannot be 'null'");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category findByName(String name) {
        checkIfCategoryIsBlank(name);
        return categoryRepository.findByName(name).orElseThrow(() -> {
            log.error(NOT_FOUND_CATEGORY_MESSAGE.formatted(name));
            throw new EntityNotFoundException(NOT_FOUND_CATEGORY_MESSAGE.formatted(name));
        });
    }

    private static void checkIfCategoryIsBlank(String name) throws UnacceptableParameterValueException {
        if (name == null || name.isEmpty()) {
            throw new UnacceptableParameterValueException("Category name is null or empty");
        }
    }
}