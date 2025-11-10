package com.ratmir.spring.hogwardsartifactsonline.util.exception.handler;

import com.ratmir.spring.hogwardsartifactsonline.dto.Result;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler({MethodArgumentNotValidException.class})
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

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result onConstraintViolation(ConstraintViolationException ex) {
        Map<String,String> map = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),   // path like methodArgName...
                        ConstraintViolation::getMessage,
                        (a,b) -> a + "; " + b
                ));

        return Result.builder()
                .flag(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
//                .data(map)
                .build();
    }

    @ExceptionHandler({HandlerMethodValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result onConstraintViolation(HandlerMethodValidationException ex) {
//        Map<String,String> map = ex.getConstraintViolations().stream()
//                .collect(Collectors.toMap(
//                        v -> v.getPropertyPath().toString(),   // path like methodArgName...
//                        ConstraintViolation::getMessage,
//                        (a,b) -> a + "; " + b
//                ));
        Map<String, String> map;
        List<ParameterValidationResult> valueResults = ex.getValueResults();
        valueResults
                .stream()
                .map(ParameterValidationResult::getResolvableErrors)
                .toList();

        return Result.builder()
                .flag(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
//                .data(map)
                .build();
    }

//    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    Result handleAuthenticationException(AuthenticationException ex) {
//        return Result.builder()
//                .flag(false)
//                .code(HttpStatus.UNAUTHORIZED.value())
//                .message("Username or password is incorrect")
//                .build();
//    }
}
