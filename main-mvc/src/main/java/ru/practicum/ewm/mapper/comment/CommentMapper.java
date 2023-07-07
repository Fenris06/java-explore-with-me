package ru.practicum.ewm.mapper.comment;

import ru.practicum.ewm.dto.comment.FullCommentDTO;
import ru.practicum.ewm.dto.comment.NewCommentDTO;
import ru.practicum.ewm.dto.comment.ShortCommentDTO;
import ru.practicum.ewm.model.comment.Comment;

public class CommentMapper {

    public static Comment fromDTO(NewCommentDTO newCommentDTO) {
        Comment comment = new Comment();
        comment.setText(newCommentDTO.getText());
        return comment;
    }

    public static FullCommentDTO toDTO(Comment comment) {
        FullCommentDTO fullDTO = new FullCommentDTO();
        fullDTO.setId(comment.getId());
        fullDTO.setOwner(comment.getOwner().getId());
        fullDTO.setEvent(comment.getEvent().getId());
        fullDTO.setText(comment.getText());
        fullDTO.setState(comment.getState());
        fullDTO.setCreateTime(comment.getCreateTime());
        return fullDTO;
    }

    public static ShortCommentDTO toShortDTO(Comment comment) {
        ShortCommentDTO shortDTO = new ShortCommentDTO();
        shortDTO.setOwnerName(comment.getOwner().getName());
        shortDTO.setText(comment.getText());
        shortDTO.setCommentDate(comment.getCreateTime());
        return shortDTO;
    }
}
