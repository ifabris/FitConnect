package hr.algebra.FitConnect.feature.exercise;

import hr.algebra.FitConnect.feature.exercise.DTO.ExerciseDTO;

import java.util.List;

public interface ExerciseService {
    ExerciseDTO addExerciseToWorkout(int workoutId, ExerciseDTO exerciseDTO);
    ExerciseDTO addExercise(ExerciseDTO exerciseDTO);
    List<ExerciseDTO> getAllExercises();
    ExerciseDTO getExerciseById(int id);
    ExerciseDTO updateExercise(int id, ExerciseDTO exerciseDTO);
    void deleteExercise(int id);
}
