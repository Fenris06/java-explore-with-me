package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.FullCommentDTO;
import ru.practicum.ewm.dto.comment.NewCommentDTO;
import ru.practicum.ewm.service.comment.CommentService;
import ru.practicum.ewm.vallidarion.Create;
import ru.practicum.ewm.vallidarion.Update;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Create.class)
    public FullCommentDTO createComment(@PathVariable("userId") Long userId,
                                        @RequestParam(name = "eventId") Long eventId,
                                        @RequestBody @Valid NewCommentDTO newCommentDTO) {
        return commentService.createComment(userId, eventId, newCommentDTO);
    }

    @PatchMapping("{commId}")
    @Validated(Update.class)
    public FullCommentDTO updateComment(@PathVariable("userId") Long userId,
                                        @PathVariable("commId") Long commId,
                                        @RequestBody @Valid NewCommentDTO newCommentDTO) {
        return commentService.updateComment(userId, commId, newCommentDTO);
    }

    @DeleteMapping("{commId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("userId") Long userId,
                              @PathVariable("commId") Long commId) {
        commentService.deleteComment(userId, commId);
    }

    @GetMapping("{commId}")
    public FullCommentDTO getById(@PathVariable("userId") Long userId,
                                  @PathVariable("commId") Long commId) {
        return commentService.getById(userId, commId);
    }

    @GetMapping
    public List<FullCommentDTO> getCommentsByOwner(@PathVariable("userId") Long userId) {
        return commentService.getCommentsByOwner(userId);
    }
}
