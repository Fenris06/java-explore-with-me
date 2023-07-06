package ru.practicum.ewm.service.comment;

import ru.practicum.ewm.dto.comment.FullCommentDTO;
import ru.practicum.ewm.dto.comment.NewCommentDTO;

import java.util.List;

public interface CommentService {

    FullCommentDTO createComment(Long userId, Long eventId, NewCommentDTO newCommentDTO);

    FullCommentDTO updateComment(Long userId, Long commId, NewCommentDTO newCommentDTO);

    void deleteComment(Long userId, Long commId);

    FullCommentDTO getById(Long userId, Long commId);

    List<FullCommentDTO> getCommentsByOwner(Long userId);
}
