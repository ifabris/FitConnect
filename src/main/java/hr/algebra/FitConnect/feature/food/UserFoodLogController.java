package hr.algebra.FitConnect.feature.food;

import hr.algebra.FitConnect.feature.food.request.LogFoodRequest;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserCoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/food-logs")
public class UserFoodLogController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private CoachFoodViewService coachFoodViewService;

    @Autowired
    private UserCoachService userCoachService;

    @PostMapping
    public ResponseEntity<UserFoodLog> logFoodForUser(@RequestBody LogFoodRequest request,
                                                      Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        int userId = currentUser.getUserId(); // Get userId from the authenticated user

        UserFoodLog log = foodService.logFoodForUser(userId, request.getFoodId(), request.getQuantity(), request.getLogDate());

        User coach = userCoachService.getCoachForUser(userId);
        if (coach == null) {
            throw new RuntimeException("No coach found for user with id: " + userId);
        }

        coachFoodViewService.saveCoachFoodView(coach.getUserId(), log);

        return ResponseEntity.status(HttpStatus.CREATED).body(log);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<UserFoodLog>> getUserFoodLogs(@PathVariable int userId,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(foodService.getUserFoodLogs(userId, date));
    }
}


