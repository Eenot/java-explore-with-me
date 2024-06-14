package ru.practicum.ewm.main.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserShortDto {

    private long id;
    private String name;
}
