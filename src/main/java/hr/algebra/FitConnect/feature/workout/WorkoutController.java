package hr.algebra.FitConnect.feature.workout;

import hr.algebra.FitConnect.feature.exercise.DTO.WorkoutDTO;
import hr.algebra.FitConnect.feature.exercise.request.WorkoutRequest;
import hr.algebra.FitConnect.feature.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(@RequestBody WorkoutRequest workoutRequest, Authentication authentication) {
        User currentCoach = (User) authentication.getPrincipal();

        if (currentCoach.getRole().getRoleId() != 2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only coaches can make workouts.");
        }

        Workout workout = new Workout();
        workout.setWorkoutName(workoutRequest.getWorkoutName());
        workout.setDescription(workoutRequest.getDescription());
        workout.setCoach(currentCoach); // Set the current coach

        WorkoutDTO newWorkout = workoutService.createWorkout(workout);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWorkout);
    }

    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<Workout>> getWorkoutsByDate(
            @PathVariable int userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // Fetch workouts based on the userId and the new workout_date
        List<Workout> workouts = workoutService.getWorkoutsByUserAndDate(userId, date);
        return ResponseEntity.ok(workouts);
    }

}


