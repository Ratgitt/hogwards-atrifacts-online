package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.validation;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.validation.impl.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

    String message() default "username already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
