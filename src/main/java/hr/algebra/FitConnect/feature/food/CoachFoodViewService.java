package hr.algebra.FitConnect.feature.food;

import java.time.LocalDate;
import java.util.List;

public interface CoachFoodViewService {
    void saveCoachFoodView(int coachId, UserFoodLog userFoodLog);

    List<UserFoodLog> getFoodLogsByUserAndDate(int userId, LocalDate date);
}

