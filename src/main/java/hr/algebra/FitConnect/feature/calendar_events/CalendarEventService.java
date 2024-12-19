package hr.algebra.FitConnect.feature.calendar_events;

import hr.algebra.FitConnect.feature.calendar_events.request.CalendarEventRequest;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CalendarEventService {

    @Autowired
    private CalendarEventRepo calendarEventRepo;

    @Autowired
    private UserRepo userRepo;

    public CalendarEvent createEvent(int coachId, String username, CalendarEventRequest request) {
        User coach = userRepo.findById(coachId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach not found"));

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        CalendarEvent event = new CalendarEvent();
        event.setCoach(coach);
        event.setUser(user);
        event.setEventName(request.getEventName());
        event.setEventDate(request.getEventDate());
        event.setDescription(request.getDescription());

        return calendarEventRepo.save(event);
    }

    public CalendarEvent getEventById(int eventId) {
        return calendarEventRepo.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }


    public List<CalendarEvent> getEventsForCoach(int coachId) {
        User coach = userRepo.findById(coachId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach not found"));
        return calendarEventRepo.findByCoach(coach);
    }

    public List<CalendarEvent> getEventsForUser(int userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return calendarEventRepo.findByUser(user);
    }

    public void deleteEvent(int eventId) {
        CalendarEvent event = calendarEventRepo.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        calendarEventRepo.delete(event);
    }

    public CalendarEvent updateEvent(int eventId, CalendarEventRequest request) {
        CalendarEvent event = calendarEventRepo.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        event.setEventName(request.getEventName());
        event.setEventDate(request.getEventDate());
        event.setDescription(request.getDescription());

        return calendarEventRepo.save(event);
    }

}
