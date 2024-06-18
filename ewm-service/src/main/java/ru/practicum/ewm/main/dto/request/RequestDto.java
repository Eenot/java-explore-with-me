package ru.practicum.ewm.main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.model.request.RequestStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RequestDto {

    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
