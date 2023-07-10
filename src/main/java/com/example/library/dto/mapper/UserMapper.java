package com.example.library.dto.mapper;

import com.example.library.dto.UserDto;
import com.example.library.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
