package ru.practicum.ewm.main.service.compilation;

import ru.practicum.ewm.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.main.dto.compilation.CompilationResponseDto;

import java.util.List;

public interface CompilationService {

    CompilationResponseDto createCompilation(CompilationDto compilation);

    CompilationResponseDto getCompilationById(long compilationId);

    List<CompilationResponseDto> getCompilationsPublic(boolean pinned, int size, int from);

    void deleteCompilation(long compilationId);

    CompilationResponseDto updateCompilation(long compilationId, CompilationDto compilation);
}
