package hr.algebra.FitConnect.feature.exercise.DTO;

import hr.algebra.FitConnect.feature.exercise.DTO.ExerciseDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkoutDTO {
    private int workoutId;
    private String workoutName;
    private String description;
    private LocalDateTime createdAt;
    private List<ExerciseDTO> exercises; // List of exercises for the workout
}
