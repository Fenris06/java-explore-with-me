package ru.practicum.ewm.dto.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ru.practicum.ewm.model.request.RequestState;

import java.util.List;
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EventRequestStatusUpdateRequestDTO {
    private List<Long> requestIds;
    private RequestState status;
}
