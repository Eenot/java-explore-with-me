package ru.practicum.ewm.main.controller.privateApi;

import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.request.RequestDto;
import ru.practicum.ewm.main.dto.event.NewEventDto;
import ru.practicum.ewm.main.dto.request.RequestListDto;
import ru.practicum.ewm.main.dto.request.RequestUpdateStatusDto;
import ru.practicum.ewm.main.service.event.EventService;
import ru.practicum.ewm.main.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/users")
public class UserControllerPrivate {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable @Min(0) long userId, @RequestBody NewEventDto event) {
        log.debug("Private: создание события пользователем с id: {}", userId);
        return eventService.createEvent(event, userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventFullDto> getUserEvents(@PathVariable @Min(0) long userId, @RequestParam(defaultValue = "0") @Min(0) int from,
                                            @RequestParam(defaultValue = "10") @Min(0) int size) {
        log.debug("Private: получение событий пользователя с id: {}", userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable @Min(0) long userId, @PathVariable @Min(0) long eventId) {
        log.debug("Private: получение события по id : {}, пользователя с id: {}",eventId, userId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable @Min(0) long userId, @PathVariable @Min(0) long eventId,
                                        @RequestBody NewEventDto event) {
        log.debug("Private: обновление события по id : {}, пользователя с id: {}",eventId, userId);
        return eventService.updateEventById(userId, eventId, event);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequestToEvent(@PathVariable @Min(0) long userId, @RequestParam @Min(0) long eventId) {
        log.debug("Private: создание запроса пользователем с id: {} к событию с id: {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<RequestDto> getUserRequests(@PathVariable @Min(0) long userId) {
        log.debug("Private: получение запросов пользователя");
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelUserRequest(@PathVariable @Min(0) long userId, @PathVariable @Min(0) long requestId) {
        log.debug("Private: отмена пользователем с id : {} запроса с id: {}", userId, requestId);
        return requestService.cancelUserRequest(userId, requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getUserEventRequests(@PathVariable @Min(0) long userId, @PathVariable @Min(0) long eventId) {
        log.debug("Private: получение запросов пользователя с id: {} события с id: {}", userId, eventId);
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestListDto changeUserEventRequestStatus(@PathVariable @Min(0) long userId,
                                                       @PathVariable @Min(0) long eventId, @RequestBody RequestUpdateStatusDto request) {
        log.debug("Private: изменение статуса запроса пользователя с id: {} события с id: {}", userId, eventId);
        return requestService.changeUserEventRequestStatus(userId, eventId, request);
    }
}

