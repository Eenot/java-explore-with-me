package ru.practicum.ewm.main.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.model.event.Location;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NewEventDto {

    private String annotation;
    private String title;
    private Integer confirmedRequests;
    private String eventDate;
    private String description;
    private Long category;
    private Location location;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
    private String stateAction;
}
