package hr.algebra.FitConnect.feature.calendar_events.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CalendarEventRequest {
    private int coachId;
    private String username;
    private String eventName;
    private LocalDateTime eventDate;
    private String description;
}

