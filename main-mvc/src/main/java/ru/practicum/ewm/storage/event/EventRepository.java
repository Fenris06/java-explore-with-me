package ru.practicum.ewm.storage.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.model.event.DataState;
import ru.practicum.ewm.model.event.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Optional<Event> findByIdAndState(Long id, DataState state);

    List<Event> findByUser_Id(Long id, Pageable pageable);

    Optional<Event> findByUser_IdAndId(Long id, Long id1);

    List<Event> findByCategory_Id(Long id, Pageable pageable);




}