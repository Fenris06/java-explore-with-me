package ru.practicum.ewm.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.HitDTO;
import ru.practicum.ewm.StatAnswerDTO;
import ru.practicum.ewm.StatDTO;
import ru.practicum.ewm.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class Controller {
    private final StatService service;

    @GetMapping("/stats")
    public List<HitDTO> cetHit(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                               @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                               @RequestParam(name = "uris", required = false, defaultValue = "") List<String> uris,
                               @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique) {
        return service.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatAnswerDTO createStat(@RequestBody @Valid StatDTO statDTO) {
        return service.createStat(statDTO);
    }
}
