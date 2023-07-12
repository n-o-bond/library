package com.example.library.service;

import com.example.library.exception.NullEntityReferenceException;
import com.example.library.model.Book;
import com.example.library.model.Category;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    public void beforeEach() {
        book = new Book();
        book.setId(UUID.fromString("ce8a0d6c-34c0-41c5-924a-5515ae96d82e"));
        book.setTitle("Math");
        book.setAuthor("John");
    }

    @Test
    public void createValidBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book actual = bookService.create(book);
        assertEquals(book, actual);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void createBookIsNull() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> bookService.create(null));
        assertTrue(exception.getMessage().contains("Book cannot be 'null'"));
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void readByIdBook() {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));

        Book actual = bookService.read(book.getId());
        assertEquals(book, actual);
        verify(bookRepository).findById(any(UUID.class));
    }

    @Test
    public void readByIdInvalidBookId() {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID notFoundId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> bookService.read(notFoundId));
        verify(bookRepository).findById(any(UUID.class));
    }

    @Test
    public void updateExistingBook() {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        book.setCategory(new Category());
        assertEquals(book, bookService.update(book));

        verify(bookRepository).save(any(Book.class));
        verify(bookRepository).findById(any(UUID.class));
    }

    @Test
    public void updateNonExistingBook(){
        assertThrows(EntityNotFoundException.class, () -> bookService.update(book));
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void updateBookIsNull() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> bookService.update(null));
        assertTrue(exception.getMessage().contains("Book cannot be 'null'"));
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void deleteExistingBook() {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));

        bookService.delete(book.getId());
        verify(bookRepository).findById(any(UUID.class));
    }

    @Test
    public void deleteInvalidBook() {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID notFoundId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> bookService.delete(notFoundId));
        verify(bookRepository).findById(any(UUID.class));
    }

    @Test
    public void findAllBooksByValidTitle(){
        var expected = new ArrayList<Book>();
        expected.add(book);

        when(bookRepository.findAllByTitle(book.getTitle())).thenReturn(expected);

        var actual = bookService.findAllBooksByTitle(book.getTitle());
        assertEquals(expected, actual);

        verify(bookRepository, times(1)).findAllByTitle(book.getTitle());
    }

    @Test
    public void findAllBooksByInvalidTitle(){
        var expected = new ArrayList<Book>();

        when(bookRepository.findAll()).thenReturn(expected);

        var actual = bookService.findAllBooksByTitle(null);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void createValidCategory() {
        Category category = new Category();
        category.setId(UUID.fromString("701c858b-b371-468d-ad62-0f653a5f279c"));
        category.setName("Fantasy");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category actual = bookService.createCategory(category);
        assertEquals(category, actual);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void createInvalidCategory() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> bookService.createCategory(null));
        assertTrue(exception.getMessage().contains("Category cannot be 'null'"));
        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    public void getAllCategories(){
        Category category = new Category();
        category.setId(UUID.fromString("701c858b-b371-468d-ad62-0f653a5f279c"));
        category.setName("Fantasy");

        var expected = new ArrayList<Category>();

        when(categoryRepository.findAll()).thenReturn(expected);

        var actual = bookService.getAllCategories();
        assertEquals(expected, actual);

        expected.add(category);
        actual = bookService.getAllCategories();
        assertEquals(expected, actual);
        verify(categoryRepository, times(2)).findAll();
    }

    @Test
    public void readByIdCategory() {
        Category category = new Category();
        category.setId(UUID.fromString("701c858b-b371-468d-ad62-0f653a5f279c"));
        category.setName("Fantasy");
        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(category));

        Category actual = bookService.readCategory(category.getId());
        assertEquals(category, actual);
        verify(categoryRepository).findById(any(UUID.class));
    }

    @Test
    public void readByIdInvalidCategoryId() {
        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID notFoundId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> bookService.readCategory(notFoundId));
        verify(categoryRepository).findById(any(UUID.class));
    }
}
