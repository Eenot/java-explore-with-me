package ru.practicum.ewm.main.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.main.dto.compilation.CompilationResponseDto;
import ru.practicum.ewm.main.dto.event.EventShortDto;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.mapper.EventMapper;
import ru.practicum.ewm.main.model.Compilation;
import ru.practicum.ewm.main.repository.CompilationRepository;
import ru.practicum.ewm.main.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.mapper.CompilationMapper.toCompilation;
import static ru.practicum.ewm.main.mapper.CompilationMapper.toCompilationResponseDto;
import static ru.practicum.ewm.main.mapper.CompilationMapper.toUpdateCompilation;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationResponseDto createCompilation(CompilationDto compilation) {
        if (compilation.getTitle().length() > 50) {
            throw new IncorrectDataException("Поле: title. Ошибка:  длина должна быть < 50. Значение: >50");

        }
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        Compilation saved = toCompilation(compilation);
        compilationRepository.save(saved);
        List<EventShortDto> events = getEventsByIds(compilation.getEvents());
        return toCompilationResponseDto(saved, events);
    }

    @Override
    public CompilationResponseDto getCompilationById(long compilationId) {
        Compilation fromDb = getCompilation(compilationId);
        List<EventShortDto> events = getEventsByIds(fromDb.getEvents());
        return toCompilationResponseDto(fromDb, events);
    }

    @Override
    public List<CompilationResponseDto> getCompilationsPublic(boolean pinned, int size, int from) {
        Pageable page = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.getCompilations(pinned, page);
        List<CompilationResponseDto> result = new ArrayList<>();

        for (Compilation c : compilations) {
            result.add(toCompilationResponseDto(c, getEventsByIds(c.getEvents())));
        }
        return result;
    }

    @Override
    public void deleteCompilation(long compilationId) {
        getCompilation(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationResponseDto updateCompilation(long compilationId, CompilationDto compilation) {
        Compilation fromDb = getCompilation(compilationId);
        List<Long> ids;

        if (compilation.getEvents() != null) {
            ids = compilation.getEvents();
        } else {
            ids = fromDb.getEvents();
        }

        if (compilation.getTitle() != null && compilation.getTitle().length() > 50) {
            throw new IncorrectDataException("Поле: title. Ошибка: длина должна быть менее 50!");
        }

        Compilation newCompilationToSave = toUpdateCompilation(fromDb, compilation, ids);

        List<EventShortDto> events = getEventsByIds(ids);
        compilationRepository.save(newCompilationToSave);

        return toCompilationResponseDto(newCompilationToSave, events);
    }

    private List<EventShortDto> getEventsByIds(List<Long> ids) {
        return eventRepository.findEventsByIds(ids).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    private Compilation getCompilation(long compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NoDataException("Подборка с id = " + compilationId + " не найдена"));
    }
}
