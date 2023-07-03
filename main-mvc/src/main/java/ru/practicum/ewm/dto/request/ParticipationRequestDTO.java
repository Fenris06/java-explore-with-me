package ru.practicum.ewm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ru.practicum.ewm.model.request.RequestState;

import java.time.LocalDateTime;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ParticipationRequestDTO {
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestState status;
}
