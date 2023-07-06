package ru.practicum.ewm.storage.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.model.event.Event;


import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Optional<Event> findByIdAndState(Long id, EventState state);

    List<Event> findByUser_Id(Long id, Pageable pageable);

    Optional<Event> findByUser_IdAndId(Long id, Long id1);

    List<Event> findByCategory_Id(Long id, Pageable pageable);


}