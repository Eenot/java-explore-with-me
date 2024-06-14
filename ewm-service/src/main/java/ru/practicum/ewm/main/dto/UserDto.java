package ru.practicum.ewm.main.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {

    private long id;
    private String name;
    private String email;
}
