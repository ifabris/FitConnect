package hr.algebra.FitConnect.feature.workout;

import hr.algebra.FitConnect.feature.exercise.DTO.ExerciseDTO;
import hr.algebra.FitConnect.feature.exercise.DTO.WorkoutDTO;
import hr.algebra.FitConnect.feature.exercise.Exercise;
import hr.algebra.FitConnect.feature.exercise.ExerciseRepo;
import hr.algebra.FitConnect.feature.userWorkout.UserWorkoutRepo;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExercise;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExerciseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {
    @Autowired
    private WorkoutRepo workoutRepo;

    @Autowired
    private ExerciseRepo exerciseRepo;

    @Autowired
    private WorkoutExerciseRepo workoutExerciseRepo; // Repository for the join table

    @Autowired
    private UserWorkoutRepo userWorkoutRepo;

    public WorkoutDTO createWorkout(Workout workout) {
        Workout savedWorkout = workoutRepo.save(workout);
        return mapToDTO(savedWorkout); // Convert to WorkoutDTO
    }

    private WorkoutDTO mapToDTO(Workout workout) {
        WorkoutDTO dto = new WorkoutDTO();
        dto.setWorkoutId(workout.getWorkoutId());
        dto.setWorkoutName(workout.getWorkoutName());
        dto.setDescription(workout.getDescription());
        dto.setCreatedAt(workout.getCreatedAt());

        // Convert exercises to DTOs
        List<ExerciseDTO> exerciseDTOs = workout.getExercises().stream()
                .map(this::mapExerciseToDTO)
                .collect(Collectors.toList());
        dto.setExercises(exerciseDTOs);

        return dto;
    }

    private ExerciseDTO mapExerciseToDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setExerciseId(exercise.getExerciseId());
        dto.setExerciseName(exercise.getExerciseName());
        dto.setSets(exercise.getSets());
        dto.setReps(exercise.getReps());
        return dto;
    }


        // Add exercise to a workout
    public Exercise addExerciseToWorkout(int workoutId, Exercise exercise) {
        // Check if workout exists
        Workout workout = workoutRepo.findById(workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        // Save the exercise first
        Exercise savedExercise = exerciseRepo.save(exercise);

        // Now save the relationship in the join table (assuming you have a WorkoutExercise entity)
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(savedExercise);

        workoutExerciseRepo.save(workoutExercise); // Save the relationship

        return savedExercise; // Return the saved exercise
    }
    public List<Workout> getWorkoutsByUserAndDate(int userId, LocalDate workoutDate) {
        return userWorkoutRepo.findByUserIdAndWorkoutDate(userId, workoutDate);
    }
}


