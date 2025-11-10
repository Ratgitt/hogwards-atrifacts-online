package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Value;

import java.time.LocalDate;

@Value
public class UserCreateDto {
    @NotBlank @Email @UniqueEmail
    String username;

    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;

    @NotBlank
    String firstname;

    String lastname;
}
