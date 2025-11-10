package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto;

import java.time.LocalDate;
import java.util.List;

public record UserFilter(

        String firstname,
        String lastname,
        LocalDate birthDate,
        List<Long> chatIds
) {
}
