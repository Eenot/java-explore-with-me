package ru.practicum.ewm.main.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.User;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "events")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 2000)
    private String annotation;
    @Column(length = 120)
    private String title;
    private int confirmedRequests;
    @DateTimeFormat(pattern = DateFormatConst)
    private LocalDateTime createdOn;
    @DateTimeFormat(pattern = DateFormatConst)
    private LocalDateTime eventDate;
    @DateTimeFormat(pattern = DateFormatConst)
    private LocalDateTime publishedOn;
    @Column(length = 7000)
    private String description;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Embedded
    private Location location;
    private Boolean isPaid;
    private Boolean isRequestModeration;
    private int participantLimit;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private Integer views;
}
