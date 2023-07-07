package ru.practicum.ewm.service.comment.impl;


import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.comment.FullCommentDTO;
import ru.practicum.ewm.dto.comment.NewCommentDTO;
import ru.practicum.ewm.dto.comment.ShortCommentDTO;
import ru.practicum.ewm.exception.DataValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.comment.CommentMapper;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.comment.CommentState;
import ru.practicum.ewm.model.comment.QComment;
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
import java.util.Set;
import java.util.stream.Collectors;

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
        return CommentMapper.toDTO(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullCommentDTO> getCommentsByOwner(Long userId, String sort, Integer from, Integer size) {
        checkUser(userId);
        PageRequest page = PageRequest.of(from / size, size);
        switch (sort) {
            case "DESC":
                return commentRepository.findByOwnerIdDesc(userId, page).stream().map(CommentMapper::toDTO).collect(Collectors.toList());
            case "ASC":
                return commentRepository.findByOwnerIdAsc(userId, page).stream().map(CommentMapper::toDTO).collect(Collectors.toList());
            default:
                throw new DataValidationException("Type of this sort " + sort + " not supported");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullCommentDTO> getAdminComments(Set<Long> events,
                                                 Set<Long> users,
                                                 String text,
                                                 CommentState state,
                                                 LocalDateTime startTime,
                                                 LocalDateTime endTime,
                                                 Integer from,
                                                 Integer size,
                                                 String sort) {
        String sorting = "createTime";
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (events != null && !events.isEmpty()) {
            booleanBuilder.and(QComment.comment.event.id.in(events));
        }
        if (users != null && !users.isEmpty()) {
            booleanBuilder.and(QComment.comment.owner.id.in(users));
        }
        if (text != null && !text.isBlank()) {
            booleanBuilder.and(QComment.comment.text.likeIgnoreCase("%" + text + "%"));
        }
        booleanBuilder.and(QComment.comment.state.eq(state));
        if (startTime != null && endTime != null) {
            booleanBuilder.and(QComment.comment.createTime.between(startTime, endTime));
        } else {
            startTime = LocalDateTime.now().minusDays(1);
            endTime = LocalDateTime.now();
            booleanBuilder.and(QComment.comment.createTime.between(startTime, endTime));
        }
        switch (sort) {
            case "DESC":
                PageRequest pageDesc = PageRequest.of(from / size, size, Sort.by(sorting).descending());
                return commentRepository.findAll(booleanBuilder, pageDesc).getContent().stream().map(CommentMapper::toDTO).collect(Collectors.toList());
            case "ASC":
                PageRequest pageAsc = PageRequest.of(from / size, size, Sort.by(sorting).ascending());
                return commentRepository.findAll(booleanBuilder, pageAsc).getContent().stream().map(CommentMapper::toDTO).collect(Collectors.toList());
            default:
                throw new DataValidationException("Type of this sort " + sort + " not supported");
        }
    }

    @Override
    public List<FullCommentDTO> updateAdminComment(Set<Long> ids, CommentState state) {
        List<Comment> update = commentRepository.findAllById(ids);
        LocalDateTime createTime = LocalDateTime.now();
        update.forEach(comment -> {
            comment.setState(state);
            comment.setCreateTime(createTime);
        });
        return commentRepository.saveAll(update)
                .stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortCommentDTO> getComments(Long eventId, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.getPubComment(eventId, CommentState.PUBLISHED, page);
        if (comments.isEmpty()) {
            throw new NotFoundException("Comment for event id=" + eventId + " not found!");
        }
        return comments
                .stream()
                .map(CommentMapper::toShortDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCancelComment(Long eventId, LocalDateTime dateBefore, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (dateBefore == null) {
            dateBefore= LocalDateTime.now();
        }
        List<Comment> remove = commentRepository.findRemoveComment(eventId, CommentState.CANCELED, dateBefore, page);
        if (remove.isEmpty()) {
            throw new NotFoundException(
                    "Canceled comment before " + dateBefore +
                            " for event id = " + eventId + " are not found"
            );
        }
        commentRepository.deleteAll(remove);
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
