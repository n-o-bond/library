package com.example.library.controller;

import com.example.library.dto.BookDto;
import com.example.library.dto.CategoryDto;
import com.example.library.dto.mapper.BookMapper;
import com.example.library.dto.mapper.CategoryMapper;
import com.example.library.model.Book;
import com.example.library.model.Category;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @GetMapping("/create/users/{userId}")
    public String createBook(@PathVariable("userId") UUID userId, Model model){
        model.addAttribute("book", new BookDto());
        return "book-create";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @PostMapping("/create/users/{userId}")
    public String createBook(@PathVariable("userId") UUID userId,
                         @Validated @ModelAttribute("book") BookDto bookDto, BindingResult result){
        if(result.hasErrors()){
            return "book-create";
        }

        Book book = bookMapper.toEntity(bookDto);
        bookService.create(book);
        return "redirect:/books/all";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{bookId}/read")
    public String readBook(@PathVariable("bookId") UUID bookId, Model model){
        Book book = bookService.read(bookId);
        BookDto bookDto = bookMapper.toDto(book);
        model.addAttribute("book", bookDto);
        return "book-info";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @GetMapping("/{bookId}/update/users/{userId}")
    public String updateBook(@PathVariable("userId") UUID userId,
                             @PathVariable("bookId") UUID bookId, Model model){
        Book book = bookService.read(bookId);
        BookDto bookDto = bookMapper.toDto(book);
        model.addAttribute("book", bookDto);
        return "book-update";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @PostMapping("/{bookId}/update/users/{userId}")
    public String updateBook(@PathVariable("userId") UUID userId,
                             @PathVariable("bookId") UUID bookId,
                             @Validated @ModelAttribute("book") BookDto bookDto, BindingResult result){
        if(result.hasErrors()){
            return "book-update";
        }
        Book oldBook = bookService.read(bookId);
        Book newBook = bookMapper.toEntity(bookDto);
        newBook.setId(oldBook.getId());
        bookService.update(newBook);
        return "redirect:/books/" + newBook.getId() + "/read";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @GetMapping("/{bookId}/delete/users/{userId}")
    public String deleteBook(@PathVariable("userId") UUID userId,
                             @PathVariable("bookId") UUID bookId){
        bookService.delete(bookId);
        return "redirect:/books/all";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public String getAllBooks(Model model,
                              @Param("title") String title){
        model.addAttribute("books", bookService.findAllBooksByTitle(title));
        model.addAttribute("title", title);
        return "books-list";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @GetMapping("/categories/create/users/{userId}")
    public String createCategory(@PathVariable("userId") UUID userId,
                                 Model model){
        model.addAttribute("category", new CategoryDto());
        return "category-create";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @PostMapping("/categories/create/users/{userId}")
    public String createCategory(@PathVariable("userId") UUID userId,
                                 @Validated @ModelAttribute("category") CategoryDto categoryDto,
                                 BindingResult result){
        if(result.hasErrors()){
            return "category-create";
        }
        Category category = categoryMapper.toEntity(categoryDto);
        bookService.createCategory(category);
        return "redirect:/books/categories/all";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories/all")
    public String getAllCategories(Model model,
                                   @Param("name") String name){
        model.addAttribute("categories", bookService.findCategoriesByName(name));
        model.addAttribute("name", name);
        return "categories-list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories/{categoryId}/read")
    public String readCategory(@PathVariable("categoryId") UUID categoryId, Model model){
        Category category = bookService.readCategory(categoryId);
        CategoryDto categoryDto = categoryMapper.toDto(category);
        model.addAttribute("category", categoryDto);
        return "category-info";
    }
}