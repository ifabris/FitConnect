package hr.algebra.FitConnect.feature.workout;

import hr.algebra.FitConnect.feature.exercise.DTO.ExerciseDTO;
import hr.algebra.FitConnect.feature.exercise.DTO.WorkoutDTO;
import hr.algebra.FitConnect.feature.exercise.Exercise;
import hr.algebra.FitConnect.feature.exercise.ExerciseRepo;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.userWorkout.UserWorkoutRepo;
import hr.algebra.FitConnect.feature.workout.request.WorkoutRequestDTO;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExercise;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExerciseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public WorkoutDTO createWorkout(WorkoutRequestDTO workoutRequestDTO, User currentCoach) {
        // Create the workout
        Workout workout = new Workout();
        workout.setWorkoutName(workoutRequestDTO.getWorkoutName());
        workout.setDescription(workoutRequestDTO.getDescription());
        workout.setCreatedAt(LocalDateTime.now());
        workout.setCoach(currentCoach); // assuming currentCoach is set somewhere else

        // Save the workout
        Workout savedWorkout = workoutRepo.save(workout);

        // Add exercises to the workout
        for (Integer exerciseId : workoutRequestDTO.getExerciseIds()) {
            Exercise exercise = exerciseRepo.findById(exerciseId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));
            WorkoutExercise workoutExercise = new WorkoutExercise();
            workoutExercise.setWorkout(savedWorkout);
            workoutExercise.setExercise(exercise);
            workoutExerciseRepo.save(workoutExercise);
        }

        return mapToDTO(savedWorkout);  // Map to DTO and return
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

    public Workout findById(int workoutId) {
        return workoutRepo.findById(workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));
    }

}
