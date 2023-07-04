package ru.practicum.ewm.model.event;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.user.User;

import javax.persistence.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "annotation", length = 2000, nullable = false)
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime created;
    @Column(name = "description", length = 7000, nullable = false)
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User user;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat", nullable = false)),
            @AttributeOverride(name = "lon", column = @Column(name = "lon", nullable = false))
    })
    private Location location;
    @Column(name = "paid", nullable = false)
    private Boolean paid;
    @Column(name = "participant_limit", nullable = false)
    private Long participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 20, nullable = false)
    private EventState state;
    @Column(name = "title", length = 120, nullable = false)
    private String title;
    @Column(name = "views")
    private Long views;
}
