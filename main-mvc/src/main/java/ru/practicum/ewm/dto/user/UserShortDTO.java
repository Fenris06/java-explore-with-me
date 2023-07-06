package ru.practicum.ewm.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserShortDTO {
    private Long id;
    private String name;
}
