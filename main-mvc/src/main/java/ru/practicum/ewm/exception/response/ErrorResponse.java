package ru.practicum.ewm.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String status;
    private final String reason;
    private final String message;
    @JsonFormat(pattern = DATE_PATTERN)
    private final LocalDateTime timestamp;
}
