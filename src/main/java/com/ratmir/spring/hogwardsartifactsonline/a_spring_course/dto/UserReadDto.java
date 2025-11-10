package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.Role;

import java.time.LocalDate;

public record UserReadDto(
        Long id,
        String username,
        LocalDate birthDate,
        String firstname,
        String lastname,
        Role role,
        CompanyReadDto companyReadDto
) {
}
