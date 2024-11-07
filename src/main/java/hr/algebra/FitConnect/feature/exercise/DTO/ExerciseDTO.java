package hr.algebra.FitConnect.feature.exercise.DTO;

import hr.algebra.FitConnect.feature.exercise.Exercise;
import lombok.Data;

@Data
public class ExerciseDTO {
    private int exerciseId;
    private String exerciseName;
    private int sets;
    private int reps;


    public static ExerciseDTO fromEntity(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setExerciseId(exercise.getExerciseId()); // Replace with your actual method to get the ID
        dto.setExerciseName(exercise.getExerciseName()); // Adjust these as needed
        dto.setSets(exercise.getSets());
        dto.setReps(exercise.getReps());
        return dto;
    }
}
