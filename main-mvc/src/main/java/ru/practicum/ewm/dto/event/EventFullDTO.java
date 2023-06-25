package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.dto.category.CategoryDTO;
import ru.practicum.ewm.dto.user.UserShortDTO;
import ru.practicum.ewm.model.event.Location;
import ru.practicum.ewm.model.event.State;

import java.time.LocalDateTime;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EventFullDTO {
    private String annotation;
    private CategoryDTO category;
    private Long confirmedRequests;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDTO initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
