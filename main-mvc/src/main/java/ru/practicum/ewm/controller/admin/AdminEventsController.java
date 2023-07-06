package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.vallidarion.Update;

import javax.validation.Valid;

import java.time.LocalDateTime;

import java.util.List;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDTO> getAdminEvents(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<EventState> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
                                             @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("{eventId}")
    @Validated(Update.class)
    public EventFullDTO updateAdminEvent(@PathVariable("eventId") Long eventId,
                                         @RequestBody @Valid NewEventDTO newEventDTO) {
        return eventService.updateAdminEvent(eventId, newEventDTO);
    }



}
