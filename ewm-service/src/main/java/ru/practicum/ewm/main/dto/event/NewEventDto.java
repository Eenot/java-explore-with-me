package ru.practicum.ewm.main.dto.event;

import ru.practicum.ewm.main.model.event.Location;
import lombok.*;

import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
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

    @Positive
    private Integer participantLimit;

    private String stateAction;
}
