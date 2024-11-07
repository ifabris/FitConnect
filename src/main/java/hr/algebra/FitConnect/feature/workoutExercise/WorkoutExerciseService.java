package hr.algebra.FitConnect.feature.workoutExercise;

import hr.algebra.FitConnect.feature.exercise.Exercise;
import hr.algebra.FitConnect.feature.exercise.ExerciseRepo;
import hr.algebra.FitConnect.feature.workout.Workout;
import hr.algebra.FitConnect.feature.workout.WorkoutRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkoutExerciseService {

    @Autowired
    private WorkoutRepo workoutRepo;

    @Autowired
    private ExerciseRepo exerciseRepo;

    @Autowired
    private WorkoutExerciseRepo workoutExerciseRepo;

    // Method to add an exercise to a workout
    public Workout addExerciseToWorkout(int workoutId, int exerciseId) {
        Workout workout = workoutRepo.findById(workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        Exercise exercise = exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

        workout.addExercise(exercise); // Use the convenience method to add the exercise
        return workoutRepo.save(workout); // Save the updated workout
    }

    // Method to remove an exercise from a workout
    public Workout removeExerciseFromWorkout(int workoutId, int exerciseId) {
        Workout workout = workoutRepo.findById(workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        Exercise exercise = exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

        workout.removeExercise(exercise); // Use the convenience method to remove the exercise
        return workoutRepo.save(workout); // Save the updated workout
    }

    public List<WorkoutExercise> getExercisesByWorkoutId(int workoutId) {
        return workoutExerciseRepo.findByWorkout_WorkoutId(workoutId);
    }
}
