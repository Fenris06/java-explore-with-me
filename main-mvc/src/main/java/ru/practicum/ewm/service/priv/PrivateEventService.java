package ru.practicum.ewm.service.priv;

import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;

import java.util.List;

public interface PrivateEventService {

    EventFullDTO createEvent(Long userId, NewEventDTO newEventDTO);

    List<EventShortDTO> getEvents(Long userId, Integer from, Integer size);

    EventFullDTO getEvent(Long userId, Long eventId);

    EventFullDTO updateEvent(Long userId, Long eventId, NewEventDTO newEventDTO);
}
