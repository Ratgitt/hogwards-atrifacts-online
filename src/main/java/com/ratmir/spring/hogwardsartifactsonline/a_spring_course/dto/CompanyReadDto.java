package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto;

import java.util.Map;

public record CompanyReadDto(
        Integer id,
        String name,
        Map<String, String> locales
) {
}
