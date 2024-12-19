package hr.algebra.FitConnect.feature.calendar_events;

import hr.algebra.FitConnect.feature.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarEventRepo extends JpaRepository<CalendarEvent, Integer> {
    List<CalendarEvent> findByCoach(User coach);
    List<CalendarEvent> findByUser(User user);

}
