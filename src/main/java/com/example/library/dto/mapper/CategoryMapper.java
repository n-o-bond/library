package com.example.library.dto.mapper;

import com.example.library.dto.CategoryDto;
import com.example.library.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto categoryDto);
    List<CategoryDto> toDtos(List<Category> categories);
}
