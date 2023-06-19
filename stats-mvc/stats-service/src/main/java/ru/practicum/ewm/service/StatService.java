package ru.practicum.ewm.service;

import ru.practicum.ewm.HitDTO;
import ru.practicum.ewm.StatAnswerDTO;
import ru.practicum.ewm.StatDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    StatAnswerDTO createStat(StatDTO statDTO);

    List<HitDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
