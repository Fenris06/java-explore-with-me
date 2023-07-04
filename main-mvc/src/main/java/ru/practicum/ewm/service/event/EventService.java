package ru.practicum.ewm.service.event;

import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;
import ru.practicum.ewm.model.event.DataState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDTO createEvent(Long userId, NewEventDTO newEventDTO);

    List<EventShortDTO> getEvents(Long userId, Integer from, Integer size);

    EventFullDTO getEvent(Long userId, Long eventId);

    EventFullDTO updateEvent(Long userId, Long eventId, NewEventDTO newEventDTO);

    EventFullDTO updateAdminEvent(Long eventId, NewEventDTO newEventDTO);

    EventFullDTO getEventById(Long id, HttpServletRequest request);

    List<EventShortDTO> getPubEvents(String text,
                                     List<Long> categories,
                                     Boolean paid,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Boolean onlyAvailable,
                                     String sort,
                                     Integer from,
                                     Integer size,
                                     HttpServletRequest request);

    List<EventFullDTO> getAdminEvents(List<Long> users, List<DataState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
