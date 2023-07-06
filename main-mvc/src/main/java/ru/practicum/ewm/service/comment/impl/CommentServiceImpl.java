package ru.practicum.ewm.service.comment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.comment.FullCommentDTO;
import ru.practicum.ewm.dto.comment.NewCommentDTO;
import ru.practicum.ewm.exception.DataValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.comment.CommentMapper;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.comment.CommentState;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.service.comment.CommentService;
import ru.practicum.ewm.storage.comment.CommentRepository;
import ru.practicum.ewm.storage.event.EventRepository;
import ru.practicum.ewm.storage.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public FullCommentDTO createComment(Long userId, Long eventId, NewCommentDTO newCommentDTO) {
        User owner = checkUser(userId);
        Event event = checkEvent(eventId);
        checkEventState(event);
        Comment comment = CommentMapper.fromDTO(newCommentDTO);
        return CommentMapper.toDTO(commentRepository.save(setCommentFields(comment, owner, event)));
    }

    @Override
    @Transactional
    public FullCommentDTO updateComment(Long userId, Long commId, NewCommentDTO newCommentDTO) {
        checkUser(userId);
        Comment update = getComment(commId);
        checkCommentOwner(userId, update);
        checkCommentState(update);
        return CommentMapper.toDTO(commentRepository.save(updateFields(update, newCommentDTO)));
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commId) {
        checkUser(userId);
        Comment comment = getComment(commId);
        checkCommentOwner(userId, comment);
        checkCommentState(comment);
        commentRepository.deleteById(commId);
    }

    @Override
    @Transactional(readOnly = true)
    public FullCommentDTO getById(Long userId, Long commId) {
        checkUser(userId);
        Comment comment = getComment(commId);
        checkCommentOwner(userId, comment);
        checkCommentState(comment);
        return CommentMapper.toDTO(comment);
    }

    @Override
    public List<FullCommentDTO> getCommentsByOwner(Long userId) {
        checkUser(userId);
        return null;
    }


    private Comment setCommentFields(Comment comment, User user, Event event) {
        LocalDateTime createTime = LocalDateTime.now();
        comment.setOwner(user);
        comment.setEvent(event);
        comment.setState(CommentState.PUBLISHED);
        comment.setCreateTime(createTime);
        return comment;
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("This user hasn't registration" + userId));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("This event id=" + eventId + " not found"));
    }

    private void checkEventState(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new DataValidationException(
                    "This event id=" + event.getId() + " not publish." +
                            " You can write comment only for publish events"
            );
        }
    }

    private Comment getComment(Long commId) {
        return commentRepository.findById(commId)
                .orElseThrow(() -> new NotFoundException("Comment id=" + commId + " not found."));
    }

    private void checkCommentOwner(Long userId, Comment comment) {
        if (!userId.equals(comment.getOwner().getId())) {
            throw new DataValidationException(
                    "You are not owner of comment id=" + comment.getId() +
                            " You can't update this comment"
            );
        }
    }

    private void checkCommentState(Comment comment) {
        if (comment.getState().equals(CommentState.CANCELED)) {
            throw new DataValidationException(
                    "Your comment id=" + comment.getId() +
                            " has been removed due to violation of the terms of use of the resource"
            );
        }
    }

    private Comment updateFields(Comment comment, NewCommentDTO newCommentDTO) {
        LocalDateTime updateCreateTime = LocalDateTime.now();
        Optional.ofNullable(newCommentDTO.getText()).ifPresent(comment::setText);
        comment.setCreateTime(updateCreateTime);
        return comment;
    }
}
