package hr.algebra.FitConnect.feature.userWorkout;

import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserServiceImpl;
import hr.algebra.FitConnect.feature.userWorkout.request.UserWorkoutRequest;
import hr.algebra.FitConnect.feature.workout.Workout;
import hr.algebra.FitConnect.feature.workout.WorkoutService;
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

    @Autowired
    private UserServiceImpl userService;  // Assuming UserService exists
    @Autowired
    private WorkoutService workoutService;  // Assuming WorkoutService exists

    @PostMapping
    public ResponseEntity<?> createUserWorkout(@RequestBody UserWorkoutRequest userWorkoutRequest) {
        // Retrieve User and Workout entities
        User user = userService.getUserById(userWorkoutRequest.getUserId());
        Workout workout = workoutService.findById(userWorkoutRequest.getWorkoutId());

        // Validate that user and workout exist
        if (user == null || workout == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user or workout ID");
        }

        // Create and populate UserWorkout entity
        UserWorkout userWorkout = new UserWorkout();
        userWorkout.setUser(user);
        userWorkout.setWorkout(workout);
        userWorkout.setWorkoutDate(userWorkoutRequest.getWorkoutDate());

        UserWorkout newUserWorkout = userWorkoutService.createUserWorkout(userWorkout);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserWorkout);
    }
}


