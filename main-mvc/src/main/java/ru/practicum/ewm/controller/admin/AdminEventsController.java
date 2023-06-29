package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.vallidarion.Update;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventsController {
    private final EventService eventService;

    @PatchMapping("{eventId}")
    @Validated(Update.class)
    public EventFullDTO updateAdminEvent(@PathVariable("eventId")  Long eventId,
                                         @RequestBody @Valid NewEventDTO newEventDTO) {
        return eventService.updateAdminEvent(eventId, newEventDTO);
    }

}
