package ru.practicum.ewm.exception;

public class DataValidationException extends RuntimeException {
    public DataValidationException(final String message) {
        super(message);
    }
}
