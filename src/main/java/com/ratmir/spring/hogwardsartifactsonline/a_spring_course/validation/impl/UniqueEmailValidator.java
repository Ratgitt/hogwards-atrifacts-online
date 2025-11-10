package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.validation.impl;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.repository.UserRepository;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.validation.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !userRepository.existsByUsername(username);
    }
}
