package ru.practicum.ewm.main.controller.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.dto.compilation.CompilationResponseDto;
import ru.practicum.ewm.main.service.compilation.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/compilations")
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationResponseDto getCompilation(@PathVariable @Min(0) long compId) {
        log.debug("Public: получение подборки с id: {}", compId);
        return compilationService.getCompilationById(compId);
    }

    @GetMapping()
    public List<CompilationResponseDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        log.debug("Public: получение подборок");
        return compilationService.getCompilationsPublic(pinned, size, from);
    }
}
