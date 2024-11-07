package hr.algebra.FitConnect.feature.calendar_events;

import hr.algebra.FitConnect.feature.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "calendar_events")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventId;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach; // Coach is a User entity

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Regular User

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "description")
    private String description;
}

