package hr.algebra.FitConnect.feature.workoutExercise;

import hr.algebra.FitConnect.feature.workout.Workout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout-exercises")
public class WorkoutExerciseController {

    @Autowired
    private WorkoutExerciseService workoutExerciseService;

    // Endpoint to add an exercise to a workout
    @PostMapping("/{workoutId}/{exerciseId}")
    public ResponseEntity<Workout> addExerciseToWorkout(@PathVariable int workoutId, @PathVariable int exerciseId) {
        Workout updatedWorkout = workoutExerciseService.addExerciseToWorkout(workoutId, exerciseId);
        return ResponseEntity.ok(updatedWorkout);
    }

    // Endpoint to remove an exercise from a workout
    @DeleteMapping("/{workoutId}/{exerciseId}")
    public ResponseEntity<Workout> removeExerciseFromWorkout(@PathVariable int workoutId, @PathVariable int exerciseId) {
        Workout updatedWorkout = workoutExerciseService.removeExerciseFromWorkout(workoutId, exerciseId);
        return ResponseEntity.ok(updatedWorkout);
    }

    @GetMapping("/{workoutId}/exercises")
    public List<WorkoutExercise> getExercisesForWorkout(@PathVariable int workoutId) {
        return workoutExerciseService.getExercisesByWorkoutId(workoutId);
    }
}
