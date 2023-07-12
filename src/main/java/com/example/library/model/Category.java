package com.example.library.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Data
public class Category {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category",  cascade = CascadeType.REMOVE)
    @Setter(AccessLevel.PRIVATE)
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book){
        books.add(book);
        book.setCategory(this);
    }

    public void removeProduct(Book book){
        books.remove(book);
        book.setCategory(null);
    }
}
