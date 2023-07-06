package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ru.practicum.ewm.model.event.Location;
import ru.practicum.ewm.constant.UserStateAction;
import ru.practicum.ewm.vallidarion.Create;
import ru.practicum.ewm.vallidarion.Update;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class NewEventDTO {
    @NotBlank(message = "Field: annotation. Error: must not be empty or have only white spase. Value: empty", groups = Create.class)
    @Size(min = 20, max = 2000, message = "Field: annotation. Error: must be min 20 char and max 2000", groups = {Create.class, Update.class})
    private String annotation;
    @NotNull(message = "Field: category. Error: must not be blank. Value: null", groups = Create.class)
    @Positive(message = "Field: category. Error: must not be positive.", groups = {Create.class, Update.class})
    private Long category;
    @NotBlank(message = "Field: description. Error: must not be empty or have only white spase. Value: empty", groups = Create.class)
    @Size(min = 20, max = 7000, message = "Field: annotation. Error: must be min 20 char and max 2000", groups = {Create.class, Update.class})
    private String description;
    @NotNull(message = "Field: eventDate. Error: must not be blank. Value: null", groups = Create.class)
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime eventDate;
    @NotNull(message = "Field: location. Error: must not be blank. Value: null", groups = Create.class)
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private UserStateAction stateAction;
    @NotBlank(message = "Field: title. Error: must not be empty or have only white spase. Value: empty", groups = Create.class)
    @Size(min = 3, max = 120, message = "Field: annotation. Error: must be min 3 char and max 120", groups = {Create.class, Update.class})
    private String title;
}


