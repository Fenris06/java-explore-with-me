package ru.practicum.ewm.storage.comment;



import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.comment.CommentState;

import java.time.LocalDateTime;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {
    @Query("select c from Comment c " +
            "where c.owner.id = :id " +
            "order by c.createTime DESC")
    List<Comment> findByOwnerIdDesc(Long id, Pageable pageable);

    @Query("select c from Comment c " +
            "where c.event.id = :id " +
            "and c.state = :state " +
            "order by c.createTime DESC")
    List<Comment> getPubComment(Long id, CommentState state, Pageable pageable);

    @Query("select c from Comment c " +
            "where c.event.id = :eventId " +
            "and c.state = :state " +
            "and c.createTime < :beforeTime " +
            "order by c.createTime DESC")
    List<Comment> findRemoveComment(Long eventId, CommentState state, LocalDateTime beforeTime, Pageable pageable);


    @Query("select c from Comment c " +
            "where c.owner.id = :id " +
            "order by c.createTime")
    List<Comment> findByOwnerIdAsc(Long id, Pageable pageable);








}