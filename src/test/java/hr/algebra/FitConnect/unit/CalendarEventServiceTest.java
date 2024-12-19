package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.calendar_events.CalendarEvent;
import hr.algebra.FitConnect.feature.calendar_events.CalendarEventRepo;
import hr.algebra.FitConnect.feature.calendar_events.CalendarEventService;
import hr.algebra.FitConnect.feature.calendar_events.request.CalendarEventRequest;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalendarEventServiceTest {

    @Mock
    private CalendarEventRepo calendarEventRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CalendarEventService calendarEventService;

    private User coach;
    private User user;
    private CalendarEventRequest request;
    private CalendarEvent event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        coach = new User();
        coach.setUserId(1);
        coach.setUsername("coach1");

        user = new User();
        user.setUserId(2);
        user.setUsername("user1");

        request = new CalendarEventRequest();
        request.setCoachId(coach.getUserId());
        request.setUsername(user.getUsername());
        request.setEventName("Test Event");
        request.setEventDate(LocalDateTime.now());
        request.setDescription("This is a test event.");

        event = new CalendarEvent();
        event.setEventId(1);
        event.setCoach(coach);
        event.setUser(user);
        event.setEventName(request.getEventName());
        event.setEventDate(request.getEventDate());
        event.setDescription(request.getDescription());
    }

    @Test
    void createEvent_shouldCreateEventSuccessfully() {
        // Mock the repository calls
        when(userRepo.findById(coach.getUserId())).thenReturn(Optional.of(coach));
        when(userRepo.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(calendarEventRepo.save(any(CalendarEvent.class))).thenReturn(event);

        // Call the service method
        CalendarEvent createdEvent = calendarEventService.createEvent(coach.getUserId(), user.getUsername(), request);

        // Verify and assert
        assertNotNull(createdEvent);
        assertEquals(request.getEventName(), createdEvent.getEventName());
        verify(calendarEventRepo, times(1)).save(any(CalendarEvent.class));
    }

    @Test
    void createEvent_shouldThrowExceptionIfCoachNotFound() {
        when(userRepo.findById(coach.getUserId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> calendarEventService.createEvent(coach.getUserId(), user.getUsername(), request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Coach not found", exception.getReason());
    }

    @Test
    void createEvent_shouldThrowExceptionIfUserNotFound() {
        when(userRepo.findById(coach.getUserId())).thenReturn(Optional.of(coach));
        when(userRepo.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> calendarEventService.createEvent(coach.getUserId(), user.getUsername(), request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    void getEventById_shouldReturnEvent() {
        when(calendarEventRepo.findById(event.getEventId())).thenReturn(Optional.of(event));

        CalendarEvent foundEvent = calendarEventService.getEventById(event.getEventId());

        assertNotNull(foundEvent);
        assertEquals(event.getEventName(), foundEvent.getEventName());
    }

    @Test
    void getEventById_shouldThrowExceptionIfEventNotFound() {
        when(calendarEventRepo.findById(event.getEventId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> calendarEventService.getEventById(event.getEventId()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Event not found", exception.getReason());
    }

    @Test
    void deleteEvent_shouldDeleteEventSuccessfully() {
        when(calendarEventRepo.findById(event.getEventId())).thenReturn(Optional.of(event));

        calendarEventService.deleteEvent(event.getEventId());

        verify(calendarEventRepo, times(1)).delete(event);
    }

    @Test
    void deleteEvent_shouldThrowExceptionIfEventNotFound() {
        when(calendarEventRepo.findById(event.getEventId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> calendarEventService.deleteEvent(event.getEventId()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Event not found", exception.getReason());
    }

    @Test
    void updateEvent_shouldUpdateEventSuccessfully() {
        when(calendarEventRepo.findById(event.getEventId())).thenReturn(Optional.of(event));
        when(calendarEventRepo.save(any(CalendarEvent.class))).thenReturn(event);

        CalendarEvent updatedEvent = calendarEventService.updateEvent(event.getEventId(), request);

        assertNotNull(updatedEvent);
        assertEquals(request.getEventName(), updatedEvent.getEventName());
        verify(calendarEventRepo, times(1)).save(event);
    }

    @Test
    void updateEvent_shouldThrowExceptionIfEventNotFound() {
        when(calendarEventRepo.findById(event.getEventId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> calendarEventService.updateEvent(event.getEventId(), request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Event not found", exception.getReason());
    }
}
