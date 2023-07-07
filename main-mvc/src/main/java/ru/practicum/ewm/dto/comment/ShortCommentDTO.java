package ru.practicum.ewm.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;


@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ShortCommentDTO {
    private String ownerName;
    private String text;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime commentDate;
}
