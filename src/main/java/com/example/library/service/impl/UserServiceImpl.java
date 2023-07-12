package com.example.library.service.impl;

import com.example.library.exception.NullEntityReferenceException;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String NULL_USER_MESSAGE = "User cannot be 'null'";
    private static final String NOT_FOUND_USER_MESSAGE = "User (id=UUID: %s) was not found";
    private static final String USER_DELETED_MESSAGE = "Used (id=UUID: %s) was deleted";

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        checkIfUserIsNull(user);
        checkIfRoleIsNull(user);
        return userRepository.save(user);
    }

    private static void checkIfUserIsNull(User user) {
        if (user == null) {
            log.error(NULL_USER_MESSAGE);
            throw new NullEntityReferenceException(NULL_USER_MESSAGE);
        }
    }

    private static void checkIfRoleIsNull(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
    }

    @Override
    public User read(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error(NOT_FOUND_USER_MESSAGE.formatted(id));
            throw new EntityNotFoundException(NOT_FOUND_USER_MESSAGE.formatted(id));
        });
    }

    @Override
    public User update(User user) {
        checkIfUserIsNull(user);
        read(user.getId());
        checkIfRoleIsNull(user);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(read(id));
        log.info(USER_DELETED_MESSAGE.formatted(id));
    }
}