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

import static ru.practicum.ewm.main.constants.LengthConstants.CompilationTitleLength;
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
        if (compilation.getTitle() == null || compilation.getTitle().isBlank()) {
            throw new IncorrectDataException("Field: title. Error: не должно быть пустым. Value: null");
        }
        if (compilation.getTitle().length() > CompilationTitleLength) {
            throw new IncorrectDataException("Field: title. Error: длина должна быть < 50. Value: >50");

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
    public void deleteCompilation(long comId) {
        getCompilation(comId);
        compilationRepository.deleteById(comId);
    }

    @Override
    public CompilationResponseDto updateCompilation(long compId, CompilationDto compilation) {
        Compilation fromDb = getCompilation(compId);
        List<Long> ids;

        if (compilation.getEvents() != null) {
            ids = compilation.getEvents();
        } else {
            ids = fromDb.getEvents();
        }

        if (compilation.getTitle() != null && compilation.getTitle().length() > CompilationTitleLength) {
            throw new IncorrectDataException("Field: title. Error: длина должна быть меньше 50!");
        }
        Compilation newCompilationToSave = toUpdateCompilation(fromDb, compilation, ids);

        List<EventShortDto> events = getEventsByIds(ids);
        compilationRepository.save(newCompilationToSave);

        return toCompilationResponseDto(newCompilationToSave, events);
    }

    @Override
    public CompilationResponseDto getCompilationById(long compId) {
        Compilation fromDb = getCompilation(compId);
        List<EventShortDto> events = getEventsByIds(fromDb.getEvents());
        return toCompilationResponseDto(fromDb, events);
    }


    private List<EventShortDto> getEventsByIds(List<Long> ids) {
        return eventRepository.findEventsByIds(ids).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    private Compilation getCompilation(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NoDataException("Подборка с id = " + compId + " не найдена"));
    }
}
