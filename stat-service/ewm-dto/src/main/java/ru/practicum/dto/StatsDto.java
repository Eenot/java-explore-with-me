package ru.practicum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StatsDto {

    private String app;
    private String uri;
    private Long hits;
}
