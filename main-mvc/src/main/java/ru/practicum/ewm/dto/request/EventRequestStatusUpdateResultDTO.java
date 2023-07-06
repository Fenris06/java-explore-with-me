package ru.practicum.ewm.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EventRequestStatusUpdateResultDTO {
    private List<ParticipationRequestDTO> confirmedRequests;
    private List<ParticipationRequestDTO> rejectedRequests;
}
