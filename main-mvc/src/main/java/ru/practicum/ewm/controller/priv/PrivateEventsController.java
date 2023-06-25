package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;

import ru.practicum.ewm.service.priv.PrivateEventService;
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
    private final PrivateEventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Create.class)
    public EventFullDTO createEvent(@PathVariable("userId") @Min(1) Long userId,
                                    @Valid @RequestBody NewEventDTO newEventDTO) {
        return service.createEvent(userId, newEventDTO);
    }

    @GetMapping
    public List<EventShortDTO> getEvents(@PathVariable("userId") @Min(1) Long userId,
                                         @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(name = "size", defaultValue = "20") @Min(1) Integer size) {
        return service.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDTO getEvent(@PathVariable("userId") @Min(1) Long userId,
                                 @Min(1) @PathVariable("eventId") Long eventId) {
        return service.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @Validated(Update.class)
    public EventFullDTO updateEvent(@PathVariable("userId") @Min(1) Long userId,
                                    @Min(1) @PathVariable("eventId") Long eventId,
                                    @RequestBody @Valid NewEventDTO newEventDTO) {
        return service.updateEvent(userId, eventId, newEventDTO);
    }
}
