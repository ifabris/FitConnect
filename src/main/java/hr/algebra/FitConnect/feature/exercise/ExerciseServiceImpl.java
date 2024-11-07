package hr.algebra.FitConnect.feature.exercise;

import hr.algebra.FitConnect.exception.ResourceNotFoundException;
import hr.algebra.FitConnect.feature.exercise.DTO.ExerciseDTO;
import hr.algebra.FitConnect.feature.workout.Workout;
import hr.algebra.FitConnect.feature.workout.WorkoutRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepo exerciseRepo;

    @Autowired
    private WorkoutRepo workoutRepo;

    @Override
    public ExerciseDTO addExerciseToWorkout(int workoutId, ExerciseDTO exerciseDTO) {
        Workout workout = workoutRepo.findById(workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        Exercise exercise = new Exercise();
        exercise.setExerciseName(exerciseDTO.getExerciseName());
        exercise.setSets(exerciseDTO.getSets());
        exercise.setReps(exerciseDTO.getReps());

        // Save exercise to the repository first
        Exercise savedExercise = exerciseRepo.save(exercise);

        // Add the saved exercise to the workout
        workout.getExercises().add(savedExercise);

        // Save the workout again to ensure the relationship is persisted
        workoutRepo.save(workout);

        return mapToDTO(savedExercise); // Return the exercise DTO
    }

    @Override
    public ExerciseDTO addExercise(ExerciseDTO exerciseDTO){
        Exercise exercise = new Exercise();
        exercise.setExerciseName(exerciseDTO.getExerciseName());
        exercise.setSets(exerciseDTO.getSets());
        exercise.setReps(exerciseDTO.getReps());

        Exercise savedExercise = exerciseRepo.save(exercise);

        return mapToDTO(savedExercise); // Return the exercise DTO
    }
    @Override
    public List<ExerciseDTO> getAllExercises() {
        return exerciseRepo.findAll().stream()
                .map(ExerciseDTO::fromEntity) // Use the static method for conversion
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseDTO getExerciseById(int id) {
        Optional<Exercise> exercise = exerciseRepo.findById(id);
        if (exercise.isPresent()) {
            return mapToDTO(exercise.get());
        }
        throw new ResourceNotFoundException("Exercise not found with ID: " + id);
    }

    @Override
    public ExerciseDTO updateExercise(int id, ExerciseDTO exerciseDTO) {
        Exercise existingExercise = exerciseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + id));

        existingExercise.setExerciseName(exerciseDTO.getExerciseName());
        existingExercise.setSets(exerciseDTO.getSets());
        existingExercise.setReps(exerciseDTO.getReps());

        Exercise updatedExercise = exerciseRepo.save(existingExercise);
        return mapToDTO(updatedExercise);
    }

    @Override
    public void deleteExercise(int id) {
        if (!exerciseRepo.existsById(id)) {
            throw new ResourceNotFoundException("Exercise not found with ID: " + id);
        }
        exerciseRepo.deleteById(id);
    }

    private ExerciseDTO mapToDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setExerciseId(exercise.getExerciseId());
        dto.setExerciseName(exercise.getExerciseName());
        dto.setSets(exercise.getSets());
        dto.setReps(exercise.getReps());
        return dto;
    }



}

