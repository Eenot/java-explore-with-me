package ru.practicum.ewm.main.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SubscriptionDto {

    private Long id;
    @NotNull
    private Long subId;
    @NotNull
    private Long initId;
    @NotNull
    private Boolean isSub;
    private LocalDateTime timestamp;

}
