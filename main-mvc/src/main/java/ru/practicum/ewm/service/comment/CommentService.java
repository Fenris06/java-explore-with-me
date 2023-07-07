package ru.practicum.ewm.service.comment;

import ru.practicum.ewm.dto.comment.FullCommentDTO;
import ru.practicum.ewm.dto.comment.NewCommentDTO;
import ru.practicum.ewm.dto.comment.ShortCommentDTO;
import ru.practicum.ewm.model.comment.CommentState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CommentService {

    FullCommentDTO createComment(Long userId, Long eventId, NewCommentDTO newCommentDTO);

    FullCommentDTO updateComment(Long userId, Long commId, NewCommentDTO newCommentDTO);

    void deleteComment(Long userId, Long commId);

    FullCommentDTO getById(Long userId, Long commId);

    List<FullCommentDTO> getCommentsByOwner(Long userId, String sort, Integer from, Integer size);

    List<FullCommentDTO> getAdminComments(Set<Long> events,
                                          Set<Long> users,
                                          String text,
                                          CommentState state,
                                          LocalDateTime startTime,
                                          LocalDateTime endTime,
                                          Integer from,
                                          Integer size,
                                          String sort);

    List<FullCommentDTO> updateAdminComment(Set<Long> ids, CommentState state);

    List<ShortCommentDTO> getComments(Long eventId, Integer from, Integer size);

   void deleteCancelComment(Long eventId, LocalDateTime dateBefore, Integer from, Integer size);
}
