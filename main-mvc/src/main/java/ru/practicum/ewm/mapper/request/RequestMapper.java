package ru.practicum.ewm.mapper.request;

import ru.practicum.ewm.dto.request.ParticipationRequestDTO;
import ru.practicum.ewm.model.request.Request;

public class RequestMapper {

    public static ParticipationRequestDTO toDTO(Request request) {
        ParticipationRequestDTO requestDTO = new ParticipationRequestDTO();
        requestDTO.setCreated(request.getCreated());
        requestDTO.setEvent(request.getEvent().getId());
        requestDTO.setId(request.getId());
        requestDTO.setRequester(request.getUser().getId());
        requestDTO.setStatus(request.getState());
        return requestDTO;
    }
}
