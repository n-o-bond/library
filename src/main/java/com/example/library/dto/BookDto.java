package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    @NotBlank
    private UUID id;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    private String description;
    @NotBlank
    private CategoryDto categoryDto;
}
