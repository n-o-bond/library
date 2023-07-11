package com.example.library.controller;

import com.example.library.dto.UserDto;
import com.example.library.dto.mapper.UserMapper;
import com.example.library.model.User;
import com.example.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/create")
    public String createUser(Model model){
        model.addAttribute("user", new UserDto());
        return "user-create";
    }

    @PostMapping("/create")
    public String createUser(@Validated @ModelAttribute("user") UserDto userDto, BindingResult result) {
        if(result.hasErrors()){
            return "user-create";
        }
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.create(user);
        return "redirect:/home";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('USER') and authentication.principal.id == #id")
    @GetMapping("/{userId}/read")
    public String readUser(@PathVariable("userId") UUID userId, Model model){
        User user = userService.read(userId);
        UserDto userDto = userMapper.toDto(user);
        model.addAttribute("user", userDto);
        return "user-info";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('USER') and authentication.principal.id == #id")
    @GetMapping("/{userId}/update")
    public String updateUser(@PathVariable("userId") UUID userId, Model model){
        User user = userService.read(userId);
        UserDto userDto = userMapper.toDto(user);
        model.addAttribute("user", userDto);
        return "user-update";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('USER') and authentication.principal.id == #id")
    @PostMapping("/{userId}/update")
    public String updateUser(@PathVariable("userId") UUID userId,
                         @Validated @ModelAttribute("user") UserDto userDto,
                         BindingResult result){
        if(result.hasErrors()){
            return "user-update";
        }
        User oldUser = userService.read(userId);
        User newUser = userMapper.toEntity(userDto);
        newUser.setId(oldUser.getId());
        userService.update(newUser);
        return "redirect:/users/" + newUser.getId() + "/read";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('USER') and authentication.principal.id == #id")
    @GetMapping("/{userId}/delete")
    public String deleteUser(@PathVariable("userId") UUID userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(((User) authentication.getPrincipal()).getId() == userId){
            userService.delete(userId);
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }
        userService.delete(userId);
        return "redirect:/login";
    }
}