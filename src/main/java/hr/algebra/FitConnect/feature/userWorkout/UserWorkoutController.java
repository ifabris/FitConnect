package hr.algebra.FitConnect.feature.userWorkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-workouts")
public class UserWorkoutController {

    @Autowired
    private UserWorkoutService userWorkoutService;

    @PostMapping
    public ResponseEntity<UserWorkout> createUserWorkout(@RequestBody UserWorkout userWorkout) {
        UserWorkout newUserWorkout = userWorkoutService.createUserWorkout(userWorkout);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserWorkout);
    }
}
