package ru.practicum.ewm.service.priv.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;
import ru.practicum.ewm.exception.DateValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.event.EventMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.State;
import ru.practicum.ewm.model.event.StateAction;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.service.priv.PrivateEventService;
import ru.practicum.ewm.storage.category.CategoryRepository;
import ru.practicum.ewm.storage.event.EventRepository;
import ru.practicum.ewm.storage.user.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository repository;

    @Override
    @Transactional
    public EventFullDTO createEvent(Long userId, NewEventDTO newEventDTO) {
        LocalDateTime crateTime = LocalDateTime.now();
        User initiator = checkUser(userId);
        Category category = checkCategory(newEventDTO.getCategory());
        checkEventDate(newEventDTO.getEventDate());
        Event event = EventMapper.fromDTO(checkOptionalFields(newEventDTO));
        event.setCategory(category);
        event.setCreated(crateTime);
        event.setUser(initiator);
        event.setPublishedOn(crateTime);
        event.setState(State.PENDING);
        return EventMapper.toDTO(repository.save(event));
    }


    @Override
    @Transactional(readOnly = true)
    public List<EventShortDTO> getEvents(Long userId, Integer from, Integer size) {
        checkUser(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return repository.findByUser_Id(userId, pageRequest)
                .stream()
                .map(EventMapper::toShortDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDTO getEvent(Long userId, Long eventId) {
        return EventMapper.toDTO(findEvent(userId, eventId));
    }

    @Override
    @Transactional
    public EventFullDTO updateEvent(Long userId, Long eventId, NewEventDTO newEventDTO) {
        Event event = findEvent(userId, eventId);
        checkEventDate(event.getEventDate());
        //TODO подумать на счет валидации на пустытые значения
        return EventMapper.toDTO(repository.save(updateEventFields(event, newEventDTO)));
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("This user hasn't registration" + userId));
    }

    private Category checkCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with this" + catId + " does not exist"));
    }

    private void checkEventDate(LocalDateTime dateTime) {
        if (dateTime.minusHours(2).isBefore(LocalDateTime.now())) {
            throw new DateValidationException("Field: eventDate." +
                    " Error: должно содержать дату," +
                    " которая еще не наступила. Value:" + dateTime);
        }
    }

    private NewEventDTO checkOptionalFields(NewEventDTO newEventDTO) {
        if (newEventDTO.getPaid() == null) {
            newEventDTO.setPaid(false);
        }
        if (newEventDTO.getParticipantLimit() == null) {
            newEventDTO.setParticipantLimit(0L);
        }
        if (newEventDTO.getRequestModeration() == null) {
            newEventDTO.setRequestModeration(true);
        }
        return newEventDTO;
    }

    private Event findEvent(Long userId, Long eventId) {
        return repository.findByUser_IdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException(
                        "Event with id=" + eventId +
                                "not found, or user with id=" + userId + "not owner of them"));
    }

    private Event updateEventFields(Event event, NewEventDTO newEventDTO) {
        if (newEventDTO.getEventDate() != null) {
            checkEventDate(newEventDTO.getEventDate());
        }
        if (newEventDTO.getStateAction() != null) {
            if (newEventDTO.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
            //TODO попробовать сделать с помощью OPTIONAL
            if (newEventDTO.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }

        }
        Optional.ofNullable(newEventDTO.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(newEventDTO.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(newEventDTO.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(newEventDTO.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(newEventDTO.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(newEventDTO.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(newEventDTO.getRequestModeration()).ifPresent(event::setRequestModeration);
        return event;
    }
}
