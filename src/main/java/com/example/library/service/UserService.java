package com.example.library.service;

import com.example.library.model.User;

import java.util.UUID;

public interface UserService {

    User create(User user);
    User read(UUID id);
    User update(User user);
    void delete(UUID id);
}