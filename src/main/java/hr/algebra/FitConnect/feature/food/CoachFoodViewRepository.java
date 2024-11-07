package hr.algebra.FitConnect.feature.food;

import hr.algebra.FitConnect.feature.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface CoachFoodViewRepository extends JpaRepository<CoachFoodView, Integer> {
    List<CoachFoodView> findByCoachAndUser(User coach, User user);

    List<CoachFoodView> findByUser_UserIdAndUserFoodLog_LogDate(int userId, LocalDate logDate);

}

