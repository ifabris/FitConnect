package hr.algebra.FitConnect.feature.calendar_events;

import hr.algebra.FitConnect.feature.calendar_events.request.CalendarEventRequest;
import hr.algebra.FitConnect.feature.user.Role;
import hr.algebra.FitConnect.feature.user.RoleType;
import hr.algebra.FitConnect.feature.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/events")
public class CalendarEventController {

    @Autowired
    private CalendarEventService calendarEventService;

    // Endpoint for coaches to create events
    @PostMapping("/create")
    public ResponseEntity<CalendarEvent> createEvent(
            @RequestBody CalendarEventRequest request,
            Authentication authentication) {
        // Fetch the coach from the authentication (assumes you have the UserDetails setup properly)
        User currentCoach = (User) authentication.getPrincipal();

        // Ensure that the current user is a coach by checking the roleId
        if (currentCoach.getRole().getRoleId() != 2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only coaches can create events.");
        }

        // Create event by passing currentCoach's ID and the user ID from request
        CalendarEvent newEvent = calendarEventService.createEvent(currentCoach.getUserId(), request.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
    }



    // Endpoint to get events for the currently authenticated coach
    @GetMapping("/coach")
    public List<CalendarEvent> getEventsForCoach(Authentication authentication) {
        User currentCoach = (User) authentication.getPrincipal();

        // Ensure the authenticated user is a coach
        if (currentCoach.getRole().getRoleId() != 2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only coaches can access their events.");
        }

        // Get events for the authenticated coach
        return calendarEventService.getEventsForCoach(currentCoach.getUserId());
    }


    // Endpoint to get events for the currently authenticated user
    @GetMapping("/user")
    public List<CalendarEvent> getEventsForUser(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole().getRoleId() != 3) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only regular users can access their events.");
        }

        // Get events for the authenticated user
        return calendarEventService.getEventsForUser(currentUser.getUserId());
    }


    // Endpoint to delete an event (only by coach or admin)
    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable int eventId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        // Check if the user is either an admin or a coach
        if (currentUser.getRole().getRoleId() != 1 && currentUser.getRole().getRoleId() != 2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only coaches or admins can delete events.");
        }

        // Proceed to delete the event
        calendarEventService.deleteEvent(eventId);
    }


    // Endpoint to update an event (only by coach)
    @PutMapping("/{eventId}")
    public CalendarEvent updateEvent(@PathVariable int eventId, @RequestBody CalendarEventRequest request, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        // Ensure that only coaches can update events
        if (currentUser.getRole().getRoleId() != 2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only coaches can update events.");
        }

        // Proceed to update the event
        return calendarEventService.updateEvent(eventId, request);
    }

}

