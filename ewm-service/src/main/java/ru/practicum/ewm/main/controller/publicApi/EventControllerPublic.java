package ru.practicum.ewm.main.controller.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.event.EventShortDto;
import ru.practicum.ewm.main.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/events")
public class EventControllerPublic {

    private final EventService eventService;
    private final HttpServletRequest request;

    @GetMapping()
    public List<EventShortDto> getEventsBySearch(@RequestParam(required = false) String text, @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid, @RequestParam(required = false) String rangeStart,
                                                 @RequestParam(required = false) String rangeEnd, @RequestParam(required = false) Boolean onlyAvailable,
                                                 @RequestParam(required = false) String sort, @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.debug("Public: поиск событий");
        return eventService.findEventsByPublicSearch(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request.getRemoteAddr());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable long eventId) {
        log.debug("Public: получение события с id: {}", eventId);
        return eventService.getUserEventByIdPublic(eventId, request.getRemoteAddr());
    }
}
