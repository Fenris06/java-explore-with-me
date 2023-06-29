package ru.practicum.ewm.service.request;

import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResultDTO;
import ru.practicum.ewm.dto.request.ParticipationRequestDTO;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequestDTO;

import java.util.List;

public interface RequestService {

    ParticipationRequestDTO createRequest(Long userId, Long eventId);

    List<ParticipationRequestDTO> getUserRequest(Long userId);

    ParticipationRequestDTO cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDTO> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResultDTO updateEventRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequestDTO updateRequestDto);
}
