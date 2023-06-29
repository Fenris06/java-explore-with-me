package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;

import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResultDTO;
import ru.practicum.ewm.dto.request.ParticipationRequestDTO;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequestDTO;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.request.RequestService;
import ru.practicum.ewm.vallidarion.Create;
import ru.practicum.ewm.vallidarion.Update;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventsController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Create.class)
    public EventFullDTO createEvent(@PathVariable("userId") Long userId,
                                    @RequestBody @Valid NewEventDTO newEventDTO) {
        return eventService.createEvent(userId, newEventDTO);
    }

    @GetMapping
    public List<EventShortDTO> getEvents(@PathVariable("userId") Long userId,
                                         @RequestParam(name = "from", defaultValue = "0")  Integer from,
                                         @RequestParam(name = "size", defaultValue = "10")  Integer size) {
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDTO getEvent(@PathVariable("userId") Long userId,
                                 @PathVariable("eventId") Long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @Validated(Update.class)
    public EventFullDTO updateEvent(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @RequestBody @Valid NewEventDTO newEventDTO) {
        return eventService.updateEvent(userId, eventId, newEventDTO);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDTO> getEventRequests(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId) {
        return requestService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResultDTO updateEventRequestStatus(@PathVariable("userId") Long userId,
                                                                      @PathVariable("eventId") Long eventId,
                                                                      @RequestBody(required = false) EventRequestStatusUpdateRequestDTO updateRequestDto) {
        return requestService.updateEventRequestStatus(userId, eventId, updateRequestDto);
    }
}
