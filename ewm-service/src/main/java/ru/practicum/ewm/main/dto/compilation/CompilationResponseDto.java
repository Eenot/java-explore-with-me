package ru.practicum.ewm.main.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.main.dto.event.EventShortDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CompilationResponseDto {

    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
