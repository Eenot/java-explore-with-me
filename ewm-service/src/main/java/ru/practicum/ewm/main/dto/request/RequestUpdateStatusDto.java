package ru.practicum.ewm.main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.model.request.RequestStatus;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RequestUpdateStatusDto {

    private List<Long> requestIds;
    private RequestStatus status;
}
