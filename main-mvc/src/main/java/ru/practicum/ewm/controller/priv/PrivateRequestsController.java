package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.request.ParticipationRequestDTO;
import ru.practicum.ewm.service.request.RequestService;


import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class PrivateRequestsController {
    private final RequestService requestService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDTO createRequest(@PathVariable("userId")  Long userId,
                                                 @RequestParam(name = "eventId")  Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDTO> getUserRequest(@PathVariable("userId")  Long userId) {
        return requestService.getUserRequest(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDTO cancelRequest(@PathVariable("userId")  Long userId,
                                                 @PathVariable("requestId")  Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
