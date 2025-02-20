package com.ratmir.spring.hogwardsartifactsonline.controller;

import com.ratmir.spring.hogwardsartifactsonline.dto.CreateUserDto;
import com.ratmir.spring.hogwardsartifactsonline.dto.Result;
import com.ratmir.spring.hogwardsartifactsonline.dto.UserDto;
import com.ratmir.spring.hogwardsartifactsonline.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Result findAllUsers() {
        List<UserDto> userDtos = userService.findAll();
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Find All Success")
                .data(userDtos)
                .build();
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Long userId) {
        UserDto foundUserDto = userService.findById(userId);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Find One Success")
                .data(foundUserDto)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        UserDto savedUserDto = userService.save(createUserDto);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.CREATED.value())
                .message("Add Success")
                .data(savedUserDto)
                .build();
    }

    @PutMapping("/{userId}")
    public Result updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserDto userDto
    ) {
        UserDto updatedUserDto = userService.update(userId, userDto);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Update Success")
                .data(updatedUserDto)
                .build();
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Success")
                .data(null)
                .build();
    }
}
