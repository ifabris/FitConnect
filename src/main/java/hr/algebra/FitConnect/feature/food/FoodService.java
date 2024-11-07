package hr.algebra.FitConnect.feature.food;

import java.time.LocalDate;
import java.util.List;

public interface FoodService {
    Food addFood(Food food);
    List<Food> getAllFoods();
    UserFoodLog logFoodForUser(int userId, int foodId, int quantity, LocalDate logDate);
    List<UserFoodLog> getUserFoodLogs(int userId, LocalDate date);
    List<CoachFoodView> getCoachViewForUser(int coachId, int userId);

    Food getFoodById(int id);
    Food updateFood(int id, Food foodDetails);
    void deleteFood(int id);
}
