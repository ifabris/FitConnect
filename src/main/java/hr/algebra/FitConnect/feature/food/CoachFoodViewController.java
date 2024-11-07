package hr.algebra.FitConnect.feature.food;

import hr.algebra.FitConnect.feature.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/coach-food-views")
public class CoachFoodViewController {

    @Autowired
    private FoodService foodService;

    @Autowired CoachFoodViewService coachFoodViewService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CoachFoodView>> getCoachViewForUser(Authentication authentication,
                                                                   @PathVariable int userId) {
        User coach = (User) authentication.getPrincipal();
        return ResponseEntity.ok(foodService.getCoachViewForUser(coach.getUserId(),userId));
    }

    @PostMapping
    public ResponseEntity<Void> createCoachFoodView(Authentication authentication, @RequestBody UserFoodLog userFoodLog) {
        User currentUser = (User) authentication.getPrincipal();
        int coachId = currentUser.getUserId(); // Get the logged-in coach's ID

        coachFoodViewService.saveCoachFoodView(coachId, userFoodLog);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{userId}/logs")
    public ResponseEntity<List<UserFoodLog>> getFoodLogsByDate(
            @PathVariable int userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<UserFoodLog> foodLogs = coachFoodViewService.getFoodLogsByUserAndDate(userId, date);
        return ResponseEntity.ok(foodLogs);
    }
}

