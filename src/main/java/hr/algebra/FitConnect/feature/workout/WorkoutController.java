package hr.algebra.FitConnect.feature.workout;

import hr.algebra.FitConnect.feature.exercise.DTO.WorkoutDTO;
import hr.algebra.FitConnect.feature.workout.request.WorkoutRequestDTO;
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
    public ResponseEntity<WorkoutDTO> createWorkout(@RequestBody WorkoutRequestDTO workoutRequestDTO, Authentication authentication) {
        User currentCoach = (User) authentication.getPrincipal();

        // Ensure only coaches can create workouts
        if (currentCoach.getRole().getRoleId() != 2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only coaches can make workouts.");
        }

        // Pass the current coach and the DTO to the service
        WorkoutDTO newWorkout = workoutService.createWorkout(workoutRequestDTO, currentCoach);

        return ResponseEntity.status(HttpStatus.CREATED).body(newWorkout);
    }

    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<Workout>> getWorkoutsByDate(
            @PathVariable int userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Workout> workouts = workoutService.getWorkoutsByUserAndDate(userId, date);
        return ResponseEntity.ok(workouts);
    }
}
