package hr.algebra.FitConnect.feature.exercise.request;

import lombok.Data;

@Data
public class ExerciseRequest {
    private String exerciseName;
    private int sets;
    private int reps;
}
