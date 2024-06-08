package ru.practicum.ewm.main.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class AdminSearchEventsParamsDto {

    private List<Long> userIds;
    private List<Long> categoriesIds;
    private List<String> states;
    private String rangeStart;
    private String rangeEnd;
    private int from;
    private int size;
}
