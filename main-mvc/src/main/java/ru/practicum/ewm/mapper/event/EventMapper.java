package ru.practicum.ewm.mapper.event;

import ru.practicum.ewm.dto.category.CategoryDTO;
import ru.practicum.ewm.dto.event.EventFullDTO;
import ru.practicum.ewm.dto.event.EventShortDTO;
import ru.practicum.ewm.dto.event.NewEventDTO;
import ru.practicum.ewm.dto.user.UserShortDTO;

import ru.practicum.ewm.model.event.Event;


public class EventMapper {

    public static Event fromDTO(NewEventDTO newEventDTO) {
        Event event = new Event();
        event.setAnnotation(newEventDTO.getAnnotation());
        event.setDescription(newEventDTO.getDescription());
        event.setEventDate(newEventDTO.getEventDate());
        event.setLocation(newEventDTO.getLocation());
        event.setPaid(newEventDTO.getPaid());
        event.setParticipantLimit(newEventDTO.getParticipantLimit());
        event.setRequestModeration(newEventDTO.getRequestModeration());
        event.setTitle(newEventDTO.getTitle());
        return event;
    }

    public static EventFullDTO toDTO(Event event) {
        EventFullDTO fullDTO = new EventFullDTO();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(event.getCategory().getId());
        categoryDTO.setName(event.getCategory().getName());
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setId(event.getUser().getId());
        userShortDTO.setName(event.getUser().getName());
        fullDTO.setAnnotation(event.getAnnotation());
        fullDTO.setCategory(categoryDTO);
        fullDTO.setConfirmedRequests(event.getConfirmedRequests());
        fullDTO.setCreatedOn(event.getCreated());
        fullDTO.setDescription(event.getDescription());
        fullDTO.setEventDate(event.getEventDate());
        fullDTO.setId(event.getId());
        fullDTO.setInitiator(userShortDTO);
        fullDTO.setLocation(event.getLocation());
        fullDTO.setPaid(event.getPaid());
        fullDTO.setParticipantLimit(event.getParticipantLimit());
        fullDTO.setPublishedOn(event.getPublishedOn());
        fullDTO.setRequestModeration(event.getRequestModeration());
        fullDTO.setState(event.getState());
        fullDTO.setTitle(event.getTitle());
        //TODO не забыть когда подключишь клинт добавить поле views
        return fullDTO;
    }

    public static EventShortDTO toShortDTO(Event event) {
        EventShortDTO shortDTO = new EventShortDTO();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(event.getCategory().getId());
        categoryDTO.setName(event.getCategory().getName());
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setId(event.getUser().getId());
        userShortDTO.setName(event.getUser().getName());
        shortDTO.setAnnotation(event.getAnnotation());
        shortDTO.setCategory(categoryDTO);
        shortDTO.setConfirmedRequests(event.getConfirmedRequests());
        shortDTO.setEventDate(event.getEventDate());
        shortDTO.setId(event.getId());
        shortDTO.setInitiator(userShortDTO);
        shortDTO.setPaid(event.getPaid());
        shortDTO.setTitle(event.getTitle());
        //TODO не забыть когда подключишь клинт добавить поле views
        return shortDTO;
    }
}
