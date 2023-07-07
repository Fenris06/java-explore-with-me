package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.ShortCommentDTO;
import ru.practicum.ewm.service.comment.CommentService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Validated
public class PubCommentController {
    private final CommentService commentService;

    @GetMapping("/{eventId}")
    public List<ShortCommentDTO> getComments(@PathVariable ("eventId") @Min(1) Long eventId,
                                             @RequestParam(name= "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(name = "size",required = false, defaultValue = "10") @Min(1) @Max(1000) Integer size) {
        return commentService.getComments(eventId, from, size);
    }
}
