package ru.practicum.ewm.exception.controller;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.DateValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.response.ErrorResponse;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandler(final NotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFormatHandler(final NumberFormatException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse emailValidException(final MethodArgumentNotValidException e) {
        FieldError error = e.getFieldError();
        if (error == null) {
            return new ErrorResponse(
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Incorrectly made request.",
                    e.getMessage(),
                    LocalDateTime.now()
            );
        } else {
            return new ErrorResponse(
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Incorrectly made request.",
                    error.getDefaultMessage(),
                    LocalDateTime.now()
            );
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse constraintHandler(final DataIntegrityViolationException e) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dateHandler(final DateValidationException e) {
        return new ErrorResponse(
                "FORBIDDEN",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

}
