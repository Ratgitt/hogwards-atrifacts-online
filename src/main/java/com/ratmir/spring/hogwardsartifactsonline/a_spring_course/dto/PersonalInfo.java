package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto;

import java.time.LocalDate;

public record PersonalInfo(
        String firstname,
        String lastname,
        LocalDate birthDate
) {
}
