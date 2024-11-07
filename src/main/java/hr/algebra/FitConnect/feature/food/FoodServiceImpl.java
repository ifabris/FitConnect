package hr.algebra.FitConnect.feature.food;

import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepo;

    @Autowired
    private UserFoodLogRepository userFoodLogRepo;

    @Autowired
    private CoachFoodViewRepository coachFoodViewRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Food addFood(Food food) {
        return foodRepo.save(food);
    }

    @Override
    public List<Food> getAllFoods() {
        return foodRepo.findAll();
    }

    @Override
    public UserFoodLog logFoodForUser(int userId, int foodId, int quantity, LocalDate logDate) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Food food = foodRepo.findById(foodId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        UserFoodLog foodLog = new UserFoodLog();
        foodLog.setUser(user);
        foodLog.setFood(food);
        foodLog.setQuantity(quantity);
        foodLog.setLogDate(logDate);

        return userFoodLogRepo.save(foodLog);
    }

    @Override
    public List<UserFoodLog> getUserFoodLogs(int userId, LocalDate date) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userFoodLogRepo.findByUserAndLogDate(user, date);
    }

    @Override
    public List<CoachFoodView> getCoachViewForUser(int coachId, int userId) {
        User coach = userRepo.findById(coachId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return coachFoodViewRepo.findByCoachAndUser(coach, user);
    }

    // Get food by ID
    @Override
    public Food getFoodById(int id) {
        return foodRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
    }

    // Update food by ID
    @Override
    public Food updateFood(int id, Food foodDetails) {
        Food existingFood = foodRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        existingFood.setFoodName(foodDetails.getFoodName()); // Assuming Food has a name field
        existingFood.setCalories(foodDetails.getCalories()); // Assuming Food has a calories field
        existingFood.setFat(foodDetails.getFat());
        existingFood.setProtein(foodDetails.getProtein());
        existingFood.setCarbs(foodDetails.getCarbs());

        return foodRepo.save(existingFood);
    }

    // Delete food by ID
    @Override
    public void deleteFood(int id) {
        Food existingFood = foodRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        foodRepo.delete(existingFood);
    }
}

