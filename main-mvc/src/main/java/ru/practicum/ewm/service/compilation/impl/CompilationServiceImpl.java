package ru.practicum.ewm.service.compilation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationDTO;
import ru.practicum.ewm.dto.compilation.NewCompilationDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.compilation.CompilationMapper;
import ru.practicum.ewm.mapper.event.EventMapper;
import ru.practicum.ewm.model.compilation.Compilation;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.service.compilation.CompilationService;
import ru.practicum.ewm.storage.compilation.CompilationRepository;
import ru.practicum.ewm.storage.event.EventRepository;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationDTO createCompilation(NewCompilationDTO newCompilationDTO) {
        Compilation compilation = CompilationMapper.fromDTO(checkFields(newCompilationDTO));
        List<Event> events = getEvents(newCompilationDTO.getEvents());
        compilation.setEvents(events);
        Compilation save = compilationRepository.save(compilation);
        CompilationDTO compilationDTO = CompilationMapper.toDTO(save);
        compilationDTO.setEvents(createShortEvents(save.getEvents()));
        return compilationDTO;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        checkCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDTO updateCompilation(Long compId, NewCompilationDTO newCompilationDTO) {
        Compilation compilation = checkCompilation(compId);
        Compilation updateFields = compilationRepository.save(updateCompilationFields(compilation, newCompilationDTO));
        CompilationDTO compilationDTO = CompilationMapper.toDTO(updateFields);
        compilationDTO.setEvents(createShortEvents(updateFields.getEvents()));
        return compilationDTO;
    }

    @Override
   @Transactional(readOnly = true)
    public List<CompilationDTO> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<CompilationDTO> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findByPinned(pinned, pageRequest).stream()
                    .map(compilation -> {
                        CompilationDTO compilationDTO = CompilationMapper.toDTO(compilation);
                        compilationDTO.setEvents(createShortEvents(compilation.getEvents()));
                        return compilationDTO;
                    })
                    .collect(Collectors.toList());
        } else {
            compilations = compilationRepository.findAll(pageRequest).stream()
                    .map(compilation -> {
                        CompilationDTO compilationDTO = CompilationMapper.toDTO(compilation);
                        compilationDTO.setEvents(createShortEvents(compilation.getEvents()));
                        return compilationDTO;
                    })
                    .collect(Collectors.toList());
        }
        return compilations;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDTO getCompilation(Long compId) {
        Compilation compilation = checkCompilation(compId);
        CompilationDTO compilationDTO = CompilationMapper.toDTO(compilation);
        compilationDTO.setEvents(createShortEvents(compilation.getEvents()));
        return compilationDTO;
    }

    private List<Event> getEvents(Set<Long> ids) {
        return eventRepository.findAllById(ids);
    }

    private NewCompilationDTO checkFields(NewCompilationDTO newCompilationDTO) {
        if (newCompilationDTO.getPinned() == null) {
            newCompilationDTO.setPinned(false);
        }
        if (newCompilationDTO.getEvents() == null) {
            newCompilationDTO.setEvents(new HashSet<>());

        }
        return newCompilationDTO;
    }

    private List<EventShortDTO> createShortEvents(List<Event> events) {
        return events.stream()
                .map(EventMapper::toShortDTO)
                .collect(Collectors.toList());
    }

    private Compilation checkCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation id=" + compId + " not found"));
    }

    private Compilation updateCompilationFields(Compilation compilation, NewCompilationDTO compilationDTO) {
        if (compilationDTO.getEvents() != null) {
            List<Event> updateEvent = getEvents(compilationDTO.getEvents());
            compilation.setEvents(updateEvent);
        }
        Optional.ofNullable(compilationDTO.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(compilationDTO.getTitle()).ifPresent(compilation::setTitle);
        return compilation;
    }
}
