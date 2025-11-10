package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.controller;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserCreateDto;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserReadDto;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.service.UserService;
import com.ratmir.spring.hogwardsartifactsonline.dto.ArtifactDto;
import com.ratmir.spring.hogwardsartifactsonline.dto.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/spring-users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{springUserId}")
    public Result findCompanyById(@PathVariable Long springUserId) {
        var userDto = userService.findById(springUserId);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Find One Success")
                .data(userDto)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserReadDto savedUserDto = userService.createUser(userCreateDto);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.CREATED.value())
                .message("Create Success")
                .data(savedUserDto)
                .build();
    }
}

