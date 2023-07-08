package ru.practicum.ewm.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.model.comment.CommentState;

import java.time.LocalDateTime;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class FullCommentDTO {
    private Long id;
    private Long owner;
    private Long event;
    private String text;
    private CommentState state;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime createTime;
}
