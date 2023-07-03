package ru.practicum.ewm.service.request.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResultDTO;
import ru.practicum.ewm.dto.request.ParticipationRequestDTO;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequestDTO;
import ru.practicum.ewm.exception.DataValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.request.RequestMapper;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.DataState;
import ru.practicum.ewm.model.request.Request;

import ru.practicum.ewm.model.request.RequestState;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.service.request.RequestService;
import ru.practicum.ewm.storage.event.EventRepository;
import ru.practicum.ewm.storage.request.RequestRepository;
import ru.practicum.ewm.storage.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDTO createRequest(Long userId, Long eventId) {
        LocalDateTime creteTime = LocalDateTime.now();
        User user = checkUser(userId);
        checkRequest(userId, eventId);
        Event event = checkEvent(eventId);
        checkEventOwner(user, event);
        Request request = createRequestFields(user, event, creteTime);
        return RequestMapper.toDTO(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDTO> getUserRequest(Long userId) {
        checkUser(userId);
        return requestRepository.findByUser_Id(userId)
                .stream()
                .map(RequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDTO cancelRequest(Long userId, Long requestId) {
        Request request = checkUserRequest(userId, requestId);
        checkRequestStatus(request);
        request.setState(RequestState.CANCELED);
        //TODO подумать на проверкой статуса cancel
        return RequestMapper.toDTO(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDTO> getEventRequests(Long userId, Long eventId) {
        checkRequestEventOwner(userId, eventId);
        return requestRepository.findByEvent_Id(eventId)
                .stream()
                .map(RequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDTO updateEventRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequestDTO updateRequestDto) {
        Event event = checkRequestEventOwner(userId, eventId);
        Long requestLimit = checkRequestLimit(event.getConfirmedRequests(), event.getParticipantLimit(), eventId);
        return updateRequestStatus(event, requestLimit, updateRequestDto);
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id=" + userId + " can't create request"));
    }

    private Event checkEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + " not found"));
        if (event.getState().equals(DataState.CANCELED) || event.getState().equals(DataState.PENDING)) {
            throw new DataValidationException(
                    "Event id=" + eventId + "not publish. " +
                            "You can't create request for this event"
            );
        }
        checkRequestLimit(event.getConfirmedRequests(), event.getParticipantLimit(), event.getId());
        return event;
    }

    private Long checkRequestLimit(Long confirmedRequests, Long participantLimit, Long eventId) {
        if (confirmedRequests >= participantLimit && participantLimit > 0) {
            throw new DataValidationException(
                    "The set of applications for participation in the event id=" + eventId +
                            " completed. You can't create request for this event"
            );
        }
        return participantLimit - confirmedRequests;
    }

    private void checkEventOwner(User user, Event event) {
        if (user.getId().equals(event.getUser().getId())) {
            throw new DataValidationException(
                    "User id=" + user.getId() +
                            " is owner event id=" + event.getId() +
                            "You cannot create a request to participate in the event you created"
            );
        }
    }

    private void checkRequest(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findByUser_IdAndEvent_Id(userId, eventId);
        if (!requests.isEmpty()) {
            throw new DataValidationException("Your request for this event=" + eventId + " has already been created");
        }
    }

    private void checkRequestStatus(Request request) {
        if (request.getState().equals(RequestState.CONFIRMED)) {
            throw new DataValidationException(
                    "Your request id=" + request.getId() +
                            " has status PUBLISHED. You can't change " +
                            "request status if status PUBLISHED."
            );
        }
    }

    private Request checkUserRequest(Long userId, Long requestId) {
        return requestRepository.findByUser_IdAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException(
                        "Request id=" + requestId +
                                " not found or user id=" + userId + " not found")
                );
    }

    private Request createRequestFields(User user, Event event, LocalDateTime createTime) {
        Request request = new Request();
        request.setCreated(createTime);
        request.setEvent(event);
        request.setUser(user);
        //TODO подумать над тем как разбить метод
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            Long newConfirmedRequests = event.getConfirmedRequests() + 1;
            event.setConfirmedRequests(newConfirmedRequests);
            request.setState(RequestState.CONFIRMED);
            requestRepository.save(request);
            eventRepository.save(event);
        } else {
            request.setState(RequestState.PENDING);
            requestRepository.save(request);
        }
        return request;
    }

    private Event checkRequestEventOwner(Long userId, Long eventId) {
//     Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("ergae"));
//        if(event.getUser().getId().equals(userId)) {
//            throw new DataValidationException(
//                    "User id=" + userId + " " +
//                            "not owner of event or " +
//                            "event id=" + eventId + " not create");
//        }
//        return event;
        return eventRepository.findByUser_IdAndId(userId, eventId)
                .orElseThrow(() -> new DataValidationException(
                        "User id=" + userId + " " +
                                "not owner of event or " +
                                "event id=" + eventId + " not create"));
    }

    private EventRequestStatusUpdateResultDTO updateRequestStatus(Event event, Long requestLimit, EventRequestStatusUpdateRequestDTO updateRequestDto) {
        List<Request> update = requestRepository.findAllById(updateRequestDto.getRequestIds());
        List<ParticipationRequestDTO> confirmed = new ArrayList<>();
        List<ParticipationRequestDTO> rejected = new ArrayList<>();
        if (event.getRequestModeration() || event.getParticipantLimit() != 0) {
            if (updateRequestDto.getStatus().equals(RequestState.CONFIRMED)) {
                update.forEach(request -> {
                    if (confirmed.size() <= requestLimit) {
                        if (request.getState().equals(RequestState.CONFIRMED) || request.getState().equals(RequestState.CANCELED)) {
                            throw new DataValidationException("You can't change request status if status is not PENDING");
                        }
                        request.setState(RequestState.CONFIRMED);
                        ParticipationRequestDTO requestDTO = RequestMapper.toDTO(request);
                        confirmed.add(requestDTO);

                    } else {
                        request.setState(RequestState.CANCELED);
                        ParticipationRequestDTO requestDTO = RequestMapper.toDTO(request);
                        rejected.add(requestDTO);
                    }
                });
            }
            if (updateRequestDto.getStatus().equals(RequestState.REJECTED)) {
                update.forEach(request -> {
                    if (request.getState().equals(RequestState.CONFIRMED) || request.getState().equals(RequestState.CANCELED)) {
                        throw new DataValidationException("You can't change request status if status is not PENDING");
                    }
                    request.setState(RequestState.REJECTED);
                    ParticipationRequestDTO requestDTO = RequestMapper.toDTO(request);
                    rejected.add(requestDTO);
                });
            }
        }
        if (!confirmed.isEmpty()) {
            Long newConfirmedRequests = event.getConfirmedRequests() + confirmed.size();
            event.setConfirmedRequests(newConfirmedRequests);
            eventRepository.save(event);
        }
        requestRepository.saveAll(update);
        return new EventRequestStatusUpdateResultDTO(confirmed, rejected);
    }
}
