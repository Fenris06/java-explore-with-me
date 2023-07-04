package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.service.event.EventService;


import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.constant.Constant.DATE_PATTERN;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class PubEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDTO> getPubEvents(@RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "categories", required = false) List<Long> categories,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
                                            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
                                            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getPubEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("{id}")
    public EventFullDTO getEvent(@PathVariable("id") Long id) {
        return eventService.getEventById(id);
    }
}

