package ru.practicum.ewm.main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.main.model.request.RequestStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class RequestDto {

    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
