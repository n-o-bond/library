package com.example.library.service;

import com.example.library.exception.NullEntityReferenceException;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setId(UUID.fromString("ce8a0d6c-34c0-41c5-924a-5515ae96d82e"));
        user.setFirstName("Linda");
        user.setLastName("Braun");
        user.setEmail("linb@gmail.com");
        user.setPassword("linnibii");
    }

    @Test
    public void createValidUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User actual = userService.create(user);
        assertEquals(user, actual);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void createUserIsNull() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> userService.create(null));
        assertTrue(exception.getMessage().contains("User cannot be 'null'"));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void readByIdUser() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        User actual = userService.read(user.getId());
        assertEquals(user, actual);
        verify(userRepository).findById(any(UUID.class));
    }

    @Test
    public void readByIdInvalidUserId() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID notFoundId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> userService.read(notFoundId));
        verify(userRepository).findById(any(UUID.class));
    }

    @Test
    public void updateExistingUser() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(user, userService.update(user));

        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(any(UUID.class));
    }

    @Test
    public void updateNonExistingUser(){
        assertThrows(EntityNotFoundException.class, () -> userService.update(user));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void updateUserIsNull() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> userService.update(null));
        assertTrue(exception.getMessage().contains("User cannot be 'null'"));
        verify(userRepository, times(0)).save(any(User.class));

    }

    @Test
    public void deleteExistingUser() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        userService.delete(user.getId());
        verify(userRepository).findById(any(UUID.class));
    }

    @Test
    public void deleteInvalidUser() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID notFoundId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> userService.delete(notFoundId));
        verify(userRepository).findById(any(UUID.class));
    }
}