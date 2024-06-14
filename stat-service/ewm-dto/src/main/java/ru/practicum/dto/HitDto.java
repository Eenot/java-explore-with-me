package ru.practicum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HitDto {

    private String uri;
    private String ip;
    private String timestamp;
    private String app;
}
