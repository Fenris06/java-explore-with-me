package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.FullCommentDTO;
import ru.practicum.ewm.model.comment.CommentState;
import ru.practicum.ewm.service.comment.CommentService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<FullCommentDTO> getAdminComments(@RequestParam(name = "events", required = false) Set<Long> events,
                                                 @RequestParam(name = "users", required = false) Set<Long> users,
                                                 @RequestParam(name = "text", required = false) String text,
                                                 @RequestParam(name = "state", required = false, defaultValue = "PUBLISHED") CommentState state,
                                                 @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime startTime,
                                                 @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime endTime,
                                                 @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(1000) Integer size,
                                                 @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort) {
        return commentService.getAdminComments(events, users, text, state, startTime, endTime, from, size, sort);
    }

    @PatchMapping
    public List<FullCommentDTO> updateAdminComment(@RequestParam(name = "ids", required = false, defaultValue = "") @Size(min = 1, max = 100) Set<Long> ids,
                                                   @RequestParam(name = "state", required = false, defaultValue = "CANCELED") CommentState state) {
        return commentService.updateAdminComment(ids, state);
    }

    @DeleteMapping("{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCancelComment(@PathVariable("eventId") @Min(1) Long eventId,
                                    @RequestParam(name = "dateBefore", required = false) @DateTimeFormat(pattern = DATE_PATTERN) @Past LocalDateTime dateBefore,
                                    @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                    @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(1000) Integer size) {
        commentService.deleteCancelComment(eventId, dateBefore, from, size);
    }
}
