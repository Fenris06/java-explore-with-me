package ru.practicum.ewm.storage.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.model.event.DataState;
import ru.practicum.ewm.model.event.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndState(Long id, DataState state);

    @Query("select e from Event e " +
            "where e.user.id in ?1 " +
            "and e.state in ?2 " +
            "and e.category.id in ?3 " +
            "and e.eventDate > ?4 " +
            "and e.eventDate < ?5")
    List<Event> getAdminEvents(@Nullable Collection<Long> ids, @Nullable Collection<DataState> states, @Nullable Collection<Long> ids1, @Nullable LocalDateTime eventDate, @Nullable LocalDateTime eventDate1, Pageable pageable);

    @Query("select e from Event e " +
            "where e.state = ?1 " +
            "and upper(e.annotation) like upper(?2) " +
            "or upper(e.description) like upper(?3) " +
            "and e.category.id in ?4 and e.paid = ?5 " +
            "and e.eventDate > ?6 " +
            "and e.eventDate < ?7")
    List<Event> gegPubEvents(DataState state, @Nullable String annotation, @Nullable String description, @Nullable Collection<Long> ids, @Nullable Boolean paid, LocalDateTime eventDate, @Nullable LocalDateTime eventDate1, Pageable pageable);

    List<Event> findByUser_Id(Long id, Pageable pageable);

    Optional<Event> findByUser_IdAndId(Long id, Long id1);

    List<Event> findByCategory_Id(Long id, Pageable pageable);




}