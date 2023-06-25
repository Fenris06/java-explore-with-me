package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.dto.category.CategoryDTO;
import ru.practicum.ewm.dto.user.UserShortDTO;


import java.time.LocalDateTime;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EventShortDTO {
    private String annotation;
    private CategoryDTO category;
    private Long confirmedRequests;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDTO initiator;
    private Boolean paid;
    private String title;
    private Long views;
}











