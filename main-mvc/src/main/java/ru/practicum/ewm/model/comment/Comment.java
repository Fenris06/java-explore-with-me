package ru.practicum.ewm.model.comment;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Column(name = "text", length = 500, nullable = false)
    private String text;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 20, nullable = false)
    private CommentState state;
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
}
