package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.validation;

import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.validation.impl.SortParamsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SortParamsValidator.class)
public @interface ValidSortParams {
    String message() default "Invalid sort parameters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * Allowed property names that client may request sorting by
     */
    String[] allowed() default {};
}
