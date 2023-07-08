package ru.practicum.ewm.dto.comment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ru.practicum.ewm.vallidarion.Create;
import ru.practicum.ewm.vallidarion.Update;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;


@Setter
@Getter
@EqualsAndHashCode
@ToString
public class NewCommentDTO {
    @NotBlank(message = "Field text. Error: can't be null, empty or has only whitespace", groups = Create.class)
    @Size(min = 3, max = 500, message = "Field text. Error: must be min 3 char and max 500", groups = {Create.class, Update.class})
    private String text;
}
