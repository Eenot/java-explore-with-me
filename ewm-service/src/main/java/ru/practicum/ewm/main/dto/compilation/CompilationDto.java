package ru.practicum.ewm.main.dto.compilation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CompilationDto {


    private List<Long> events;
    private Boolean pinned;
    private String title;
}
