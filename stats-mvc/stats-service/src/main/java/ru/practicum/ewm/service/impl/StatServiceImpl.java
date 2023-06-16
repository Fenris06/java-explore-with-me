package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.HitDTO;
import ru.practicum.ewm.StatAnswerDTO;
import ru.practicum.ewm.StatDTO;
import ru.practicum.ewm.mapper.Mapper;
import ru.practicum.ewm.model.Stat;
import ru.practicum.ewm.service.StatService;
import ru.practicum.ewm.storage.StatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Override
    @Transactional
    public StatAnswerDTO createStat(StatDTO statDTO) {
        Stat stat = Mapper.fromDTO(statDTO);
        return Mapper.toDto(repository.save(stat));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HitDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris.isEmpty()) {
            return repository.getStatAll(start, end)
                    .stream()
                    .map(Mapper::hitToDTO)
                    .collect(Collectors.toList());
        }
        if (!unique) {
            return repository.getStat(start, end, uris)
                    .stream()
                    .map(Mapper::hitToDTO)
                    .collect(Collectors.toList());
        }
        return repository.getStatDistinct(start, end, uris)
                .stream()
                .map(Mapper::hitToDTO)
                .collect(Collectors.toList());
    }
}
