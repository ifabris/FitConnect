package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.exercise.DTO.ExerciseDTO;
import hr.algebra.FitConnect.feature.exercise.Exercise;
import hr.algebra.FitConnect.feature.exercise.ExerciseRepo;
import hr.algebra.FitConnect.feature.exercise.ExerciseServiceImpl;
import hr.algebra.FitConnect.feature.workout.Workout;
import hr.algebra.FitConnect.feature.workout.WorkoutRepo;
import hr.algebra.FitConnect.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciseServiceImplTest {

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @Mock
    private ExerciseRepo exerciseRepo;

    @Mock
    private WorkoutRepo workoutRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddExerciseToWorkout_Success() {
        int workoutId = 1;
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setExerciseName("Push Up");
        exerciseDTO.setSets(3);
        exerciseDTO.setReps(15);

        Workout workout = new Workout();
        workout.setWorkoutId(workoutId);

        when(workoutRepo.findById(workoutId)).thenReturn(Optional.of(workout));
        when(exerciseRepo.save(any(Exercise.class))).thenAnswer(invocation -> {
            Exercise exercise = invocation.getArgument(0);
            exercise.setExerciseId(1);
            return exercise;
        });

        ExerciseDTO result = exerciseService.addExerciseToWorkout(workoutId, exerciseDTO);

        assertNotNull(result);
        assertEquals("Push Up", result.getExerciseName());
        assertEquals(3, result.getSets());
        assertEquals(15, result.getReps());

        verify(workoutRepo).save(workout);
    }

    @Test
    void testAddExerciseToWorkout_WorkoutNotFound() {
        int workoutId = 1;
        ExerciseDTO exerciseDTO = new ExerciseDTO();

        when(workoutRepo.findById(workoutId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> exerciseService.addExerciseToWorkout(workoutId, exerciseDTO));
    }

    @Test
    void testAddExercise_Success() {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setExerciseName("Squat");
        exerciseDTO.setSets(3);
        exerciseDTO.setReps(10);

        when(exerciseRepo.save(any(Exercise.class))).thenAnswer(invocation -> {
            Exercise exercise = invocation.getArgument(0);
            exercise.setExerciseId(1);
            return exercise;
        });

        ExerciseDTO result = exerciseService.addExercise(exerciseDTO);

        assertNotNull(result);
        assertEquals("Squat", result.getExerciseName());
        assertEquals(3, result.getSets());
        assertEquals(10, result.getReps());
    }

    @Test
    void testGetExerciseById_Success() {
        int exerciseId = 1;
        Exercise exercise = new Exercise();
        exercise.setExerciseId(exerciseId);
        exercise.setExerciseName("Pull Up");
        exercise.setSets(4);
        exercise.setReps(8);

        when(exerciseRepo.findById(exerciseId)).thenReturn(Optional.of(exercise));

        ExerciseDTO result = exerciseService.getExerciseById(exerciseId);

        assertNotNull(result);
        assertEquals("Pull Up", result.getExerciseName());
        assertEquals(4, result.getSets());
        assertEquals(8, result.getReps());
    }

    @Test
    void testGetExerciseById_NotFound() {
        int exerciseId = 1;

        when(exerciseRepo.findById(exerciseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> exerciseService.getExerciseById(exerciseId));
    }

    @Test
    void testUpdateExercise_Success() {
        int exerciseId = 1;
        Exercise existingExercise = new Exercise();
        existingExercise.setExerciseId(exerciseId);
        existingExercise.setExerciseName("Bench Press");
        existingExercise.setSets(3);
        existingExercise.setReps(12);

        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setExerciseName("Deadlift");
        exerciseDTO.setSets(4);
        exerciseDTO.setReps(6);

        when(exerciseRepo.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepo.save(any(Exercise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExerciseDTO result = exerciseService.updateExercise(exerciseId, exerciseDTO);

        assertNotNull(result);
        assertEquals("Deadlift", result.getExerciseName());
        assertEquals(4, result.getSets());
        assertEquals(6, result.getReps());
    }

    @Test
    void testUpdateExercise_NotFound() {
        int exerciseId = 1;
        ExerciseDTO exerciseDTO = new ExerciseDTO();

        when(exerciseRepo.findById(exerciseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> exerciseService.updateExercise(exerciseId, exerciseDTO));
    }

    @Test
    void testDeleteExercise_Success() {
        int exerciseId = 1;

        when(exerciseRepo.existsById(exerciseId)).thenReturn(true);

        assertDoesNotThrow(() -> exerciseService.deleteExercise(exerciseId));

        verify(exerciseRepo).deleteById(exerciseId);
    }

    @Test
    void testDeleteExercise_NotFound() {
        int exerciseId = 1;

        when(exerciseRepo.existsById(exerciseId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> exerciseService.deleteExercise(exerciseId));
    }
}
