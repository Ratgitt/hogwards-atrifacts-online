package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.validation.impl;

import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.validation.ValidSortParams;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SortParamsValidator implements ConstraintValidator<ValidSortParams, Pageable> {

    private Set<String> allowed;

    @Override
    public void initialize(ValidSortParams constraintAnnotation) {
        this.allowed = Arrays.stream(constraintAnnotation.allowed())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        if (pageable == null) return true; // other validations can cover null

        Sort sort = pageable.getSort();
        if (sort.isUnsorted()) return true;

        List<String> invalid = new ArrayList<>();
        for (Sort.Order order : sort) {
            String prop = order.getProperty();
            if (!allowed.contains(prop)) {
                invalid.add(prop);
            }
        }

        if (invalid.isEmpty()) {
            return true;
        }

        // build detailed violation message (one violation per bad property)
        context.disableDefaultConstraintViolation();
        for (String bad : invalid) {
            context.buildConstraintViolationWithTemplate("Invalid sort field: " + bad)
                    .addConstraintViolation();
        }
        return false;
    }
}
