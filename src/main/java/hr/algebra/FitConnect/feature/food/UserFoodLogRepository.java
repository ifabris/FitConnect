package hr.algebra.FitConnect.feature.food;

import hr.algebra.FitConnect.feature.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserFoodLogRepository extends JpaRepository<UserFoodLog, Integer> {
    List<UserFoodLog> findByUserAndLogDate(User user, LocalDate logDate);
}
