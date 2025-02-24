package com.ratmir.spring.hogwardsartifactsonline.util.exception.handler;

import com.ratmir.spring.hogwardsartifactsonline.dto.Result;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    
    public static final String VALIDATION_ERROR_MESSAGE = "Provided arguments are invalid, see data for details.";

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundException(Exception ex) {
        return Result.builder()
                .flag(false)
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach(error -> {
            String key = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            map.put(key, value);
        });
        return Result.builder()
                .flag(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_ERROR_MESSAGE)
                .data(map)
                .build();
    }
}
