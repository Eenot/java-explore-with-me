package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.event.EventShortDto;
import ru.practicum.ewm.main.dto.event.NewEventDto;
import ru.practicum.ewm.main.dto.search.AdminSearchEventsParamsDto;
import ru.practicum.ewm.main.dto.search.PublicSearchEventsParamsDto;
import ru.practicum.ewm.main.exception.ConflictDataException;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.mapper.EventMapper;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.event.EventState;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    @Override
    public EventFullDto createEvent(NewEventDto eventDto, long userId) {
        if (eventDto.getCategory() == null) {
            throw new IncorrectDataException("Поле: category. Ошибка: не может быть пустым. Значение: null");
        }
        if (eventDto.getAnnotation() == null) {
            throw new IncorrectDataException("Поле: annotation. Ошибка: не может быть пустым. Значение: null");
        }
        if (eventDto.getEventDate() == null) {
            throw new IncorrectDataException("Поле: event date. Ошибка: не может быть пустым. Значение: null");
        }
        if (eventDto.getDescription() == null) {
            throw new IncorrectDataException("Поле: description. Ошибка: не может быть пустым. Значение: null");
        }
        if (eventDto.getLocation() == null) {
            throw new IncorrectDataException("Поле: location. Ошибка: не может быть пустым. Значение: null");
        }
        if (eventDto.getTitle() == null) {
            throw new IncorrectDataException("Поле: title. Ошибка: не может быть пустым. Значение: null");
        }

        if (eventDto.getDescription().isBlank()) {
            throw new IncorrectDataException("Поле: description. Ошибка: не может быть пустым. Значение: blank");
        }
        if (eventDto.getAnnotation().isBlank()) {
            throw new IncorrectDataException("Поле: annotation. Ошибка: не может быть пустым. Значение: blank");
        }

        checkAboutEventInfo(eventDto);

        LocalDateTime eventTime = EventMapper.toDateFromString(eventDto.getEventDate());
        if (LocalDateTime.now().until(eventTime, ChronoUnit.HOURS) < 2) {
            throw new IncorrectDataException("Поле: eventDate. Ошибка: должно содержать дату, которая еще не наступила. Значение: " + eventDto.getEventDate());
        }

        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(0);
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(true);
        }

        User initiator = findUserById(userId);
        Category category = findCategoryById(eventDto.getCategory());
        Event event = EventMapper.toEventFromNewEvent(eventDto, initiator, category, LocalDateTime.now());
        eventRepository.save(event);
        return EventMapper.toEventFullDtoFromEvent(event);
    }

    @Override
    public List<EventFullDto> getUserEvents(long userId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventRepository.getUserEvents(userId, page).stream()
                .map(EventMapper::toEventFullDtoFromEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEventById(long userId, long eventId) {
        findUserById(userId);
        return EventMapper.toEventFullDtoFromEvent(eventRepository.findEventById(userId, eventId)
                .orElseThrow(() -> new NoDataException("Событие с id = " + eventId + " не найдено")));
    }

    @Override
    public EventFullDto updateEventById(long userId, long eventId, NewEventDto newEvent) {
        Event eventFromDb = eventRepository.findEventById(userId, eventId)
                .orElseThrow(() -> new NoDataException("Событие с id = " + eventId + " не найдено"));
        Category category = null;
        if (newEvent.getCategory() != null) {
            category = findCategoryById(newEvent.getCategory());
        }
        if (eventFromDb.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictDataException("Только отменённые события или события в ожидании могут быть изменены");
        }
        if (newEvent.getEventDate() != null && !newEvent.getEventDate().isEmpty()) {
            if (LocalDateTime.now().until(EventMapper.toDateFromString(newEvent.getEventDate()), ChronoUnit.HOURS) < 2) {
                throw new IncorrectDataException("Поле: eventDate. Ошибка: должно содержать дату, которая еще не наступила." +
                        " Значение: " + newEvent.getEventDate());
            }
        }
        checkAboutEventInfo(newEvent);

        Event newMappedEvent = EventMapper.toEventUpdate(eventFromDb, newEvent, category);
        eventRepository.save(newMappedEvent);
        return EventMapper.toEventFullDtoFromEvent(newMappedEvent);
    }

    @Override
    public List<EventFullDto> findEventsBySearch(AdminSearchEventsParamsDto params) {
        Pageable page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());

        List<EventState> eventStates;

        if (params.getStates() != null) {
            eventStates = params.getStates().stream()
                    .map(EventState::convertToEventState)
                    .collect(Collectors.toList());
        } else {
            eventStates = null;
        }

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (params.getRangeStart() != null) {
            startDate = EventMapper.toDateFromString(params.getRangeStart());
        }

        if (params.getRangeEnd() != null) {
            endDate = EventMapper.toDateFromString(params.getRangeEnd());
        }
        return eventRepository.findEventsBySearchWithSpec(params.getUserIds(), params.getCategoriesIds(), eventStates,
                        startDate, endDate, page)
                .stream()
                .map(EventMapper::toEventFullDtoFromEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(long eventId, NewEventDto event) {
        Event eventFromDb = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoDataException("Событие с id = " + eventId + " не найдено"));
        LocalDateTime publishedTime = LocalDateTime.now();
        if (event.getEventDate() != null && !event.getEventDate().isEmpty()) {
            if (publishedTime.until(EventMapper.toDateFromString(event.getEventDate()), ChronoUnit.HOURS) < 1) {
                throw new IncorrectDataException("Поле: eventDate. Ошибка: разница между временем должна быть не меньше 1 часа." +
                        " Значение: " + event.getEventDate());
            }
        }
        if (eventFromDb.getState().equals(EventState.CANCELED)) {
            throw new ConflictDataException("Поле: eventState. Ошибка: state не должен быть CANCELED для публикации" +
                    " Значение: CANCELED");
        }
        if (eventFromDb.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictDataException("Поле: eventState. Ошибка: event уже опубликован. Значение: PUBLISHED");
        }

        checkAboutEventInfo(event);

        Category category = null;
        if (event.getCategory() != null) {
            category = findCategoryById(event.getCategory());
        }
        Event newEvent = EventMapper.toEventUpdateByAdmin(eventFromDb, event, category, publishedTime);
        eventRepository.save(newEvent);
        return EventMapper.toEventFullDtoFromEvent(newEvent);
    }

    @Override
    public List<EventShortDto> findEventsByPublicSearch(PublicSearchEventsParamsDto params) {
        saveStat("/events", params.getIp());

        Pageable page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());
        LocalDateTime start;
        LocalDateTime end = null;

        if (params.getRangeStart() == null) {
            start = LocalDateTime.now();
        } else {
            start = EventMapper.toDateFromString(params.getRangeStart());
        }

        if (params.getRangeEnd() != null) {
            end = EventMapper.toDateFromString(params.getRangeEnd());
            if (start.isAfter(end)) {
                throw new IncorrectDataException("Поле: endDate. Ошибка: конец события не может быть в прошлом");
            }
        }

        if (params.getOnlyAvailable() == null) {
            params.setOnlyAvailable(false);
        }

        List<Event> result;

        if (params.getOnlyAvailable()) {
            result = eventRepository.findEventsByPublicSearchOnlyAvailableWithSpec(params.getText(), params.getCategories(),
                    params.getPaid(), start, end, EventState.PUBLISHED, page);
        } else {
            result = eventRepository.findEventsByPublicSearchWithSpec(params.getText(), params.getCategories(), params.getPaid(),
                    start, end, EventState.PUBLISHED, page);
        }

        if (params.getSort() == null) {
            params.setSort("EVENT_DATE");
        }

        switch (params.getSort()) {
            case "EVENT_DATE": {
                result = result.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
                break;
            }
            case "VIEWS": {
                result = result.stream()
                        .sorted(Comparator.comparingLong(x -> -x.getViews()))
                        .collect(Collectors.toList());
                break;
            }
        }

        return result.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEventByIdPublic(long eventId, String ip) {
        Event eventFromDb = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoDataException("Событие с id = " + eventId + " не найдено"));
        if (!eventFromDb.getState().equals(EventState.PUBLISHED)) {
            throw new NoDataException("Поле: eventState. Ошибка: event еще не опубликован. Значение: " + eventFromDb.getState());
        }
        saveStat("/events/" + eventId, ip);
        String body = statsClient.getStats("2020-05-05 00:00:00", "2050-05-05 00:00:00",
                Collections.singletonList("/events/" + eventId), true).getBody().toString();
        eventFromDb.setViews(Integer.parseInt(body.substring(body.lastIndexOf("=") + 1, body.length() - 2)));
        return EventMapper.toEventFullDtoFromEvent(eventFromDb);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoDataException("Пользователь с id = " + userId + " не найден"));
    }

    private Category findCategoryById(long categoryId) {
        return categoryRepository.findById((categoryId))
                .orElseThrow(() -> new NoDataException("Категория с id = " + categoryId + " не найдена"));
    }

    private void saveStat(String uri, String ip) {
        HitDto hit = HitDto.builder()
                .app("ewm-service")
                .timestamp(EventMapper.toStringFromDate(LocalDateTime.now()))
                .uri(uri)
                .ip(ip)
                .build();
        statsClient.createHit(hit);
    }

    private void checkAboutEventInfo(NewEventDto newEvent) {
        if (newEvent.getAnnotation() != null) {
            if (newEvent.getAnnotation().length() > 2000 || newEvent.getAnnotation().length() < 20) {
                throw new IncorrectDataException("Поле: annotation. Ошибка: недопустимая длина");
            }
        }
        if (newEvent.getDescription() != null) {
            if (newEvent.getDescription().length() > 7000 || newEvent.getDescription().length() < 20) {
                throw new IncorrectDataException("Поле: desc. Ошибка: недопустимая длина");
            }
        }
        if (newEvent.getTitle() != null) {
            if (newEvent.getTitle().length() > 120 || newEvent.getTitle().length() < 3) {
                throw new IncorrectDataException("Прое: title. Ошибка: недопустимая длина");
            }
        }
    }
}