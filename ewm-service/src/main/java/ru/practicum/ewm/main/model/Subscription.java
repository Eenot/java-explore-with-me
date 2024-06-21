package ru.practicum.ewm.main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static ru.practicum.ewm.main.constants.DateTimeFormatConstant.DateFormatConst;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "subscriptions")
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private User subscriber;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    private boolean isSubscribed;
    @DateTimeFormat(pattern = DateFormatConst)
    private LocalDateTime timestamp;
}
