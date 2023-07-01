package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;
import ru.practicum.ewm.model.event.DataState;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.vallidarion.Update;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventsController {
    private final EventService eventService;

    @PatchMapping("{eventId}")
    @Validated(Update.class)
    public EventFullDTO updateAdminEvent(@PathVariable("eventId") Long eventId,
                                         @RequestBody @Valid NewEventDTO newEventDTO) {
        return eventService.updateAdminEvent(eventId, newEventDTO);
    }

    @GetMapping
    public List<EventFullDTO> getAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "states", required = false) List<DataState> states,
                                             @RequestParam(name = "categories", required = false) List<Long> categories,
                                             @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
                                             @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

}
