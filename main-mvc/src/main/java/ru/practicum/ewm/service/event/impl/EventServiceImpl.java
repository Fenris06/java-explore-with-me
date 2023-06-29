package ru.practicum.ewm.service.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;
import ru.practicum.ewm.exception.ArgumentException;
import ru.practicum.ewm.exception.DataValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.event.EventMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.DataState;
import ru.practicum.ewm.model.event.UserStateAction;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.storage.category.CategoryRepository;
import ru.practicum.ewm.storage.event.EventRepository;
import ru.practicum.ewm.storage.user.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EventFullDTO createEvent(Long userId, NewEventDTO newEventDTO) {
        LocalDateTime crateTime = LocalDateTime.now();
        User initiator = checkUser(userId);
        Category category = checkCategory(newEventDTO.getCategory());
        checkEventDate(newEventDTO.getEventDate());
        Event event = EventMapper.fromDTO(checkOptionalFields(newEventDTO));
        //TODO убрать в отдельный метод
        event.setCategory(category);
        event.setCreated(crateTime);
        event.setUser(initiator);
        event.setPublishedOn(crateTime);
        //TODO сделать отдельной переменной
        event.setConfirmedRequests(0L);
        event.setState(DataState.PENDING);
        return EventMapper.toDTO(eventRepository.save(event));
    }


    @Override
    @Transactional(readOnly = true)
    public List<EventShortDTO> getEvents(Long userId, Integer from, Integer size) {
        checkUser(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return eventRepository.findByUser_Id(userId, pageRequest)
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
        checkEventStatusByUser(event.getState());
        //TODO подумать на счет валидации на значения состоящие из пробелов
        return EventMapper.toDTO(eventRepository.save(updateEventFields(event, newEventDTO)));
    }

    @Override
    @Transactional
    public EventFullDTO updateAdminEvent(Long eventId, NewEventDTO newEventDTO) {
        //TODO подумать над объединением методов find events(null)
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + "not found"));
        checkEventDate(event.getEventDate());
        checkEventStatusByAdmin(event.getState());
        return EventMapper.toDTO(eventRepository.save(updateEventFields(event, newEventDTO)));
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
            throw new ArgumentException("Field: eventDate." +
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
        return eventRepository.findByUser_IdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException(
                        "Event with id=" + eventId +
                                "not found, or user with id=" + userId + "not owner of them"));
    }

    private Event updateEventFields(Event event, NewEventDTO newEventDTO) {
        if (newEventDTO.getEventDate() != null) {
            checkEventDate(newEventDTO.getEventDate());
        }
        if (newEventDTO.getStateAction() != null) {
            if (newEventDTO.getStateAction().equals(UserStateAction.CANCEL_REVIEW)) {
                event.setState(DataState.CANCELED);
            }
            //TODO попробовать сделать с помощью OPTIONAL и подумать над локацией и над разбивкой статусов по юзеру и админу
            if (newEventDTO.getStateAction().equals(UserStateAction.SEND_TO_REVIEW)) {
                event.setState(DataState.PENDING);
            }
            if (newEventDTO.getStateAction().equals(UserStateAction.REJECT_EVENT)) {
                event.setState(DataState.CANCELED);
            }
            if (newEventDTO.getStateAction().equals(UserStateAction.PUBLISH_EVENT)) {
                event.setState(DataState.PUBLISHED);
            }

        }
        Optional.ofNullable(newEventDTO.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(newEventDTO.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(newEventDTO.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(newEventDTO.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(newEventDTO.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(newEventDTO.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(newEventDTO.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(newEventDTO.getTitle()).ifPresent(event::setTitle);
        return event;
    }

    private void checkEventStatusByUser(DataState state) {
        if (state.equals(DataState.PUBLISHED)) {
            throw new DataValidationException("Only pending and cancel events can be changed");
        }
    }

    private void checkEventStatusByAdmin(DataState state) {
        if (state.equals(DataState.PUBLISHED)) {
            throw new DataValidationException("Only pending  events can be changed");
        }
        if (state.equals(DataState.CANCELED)) {
            throw new DataValidationException("Only pending events can be changed");
        }
    }
}
