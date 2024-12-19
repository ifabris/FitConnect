package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.exercise.Exercise;
import hr.algebra.FitConnect.feature.exercise.ExerciseRepo;
import hr.algebra.FitConnect.feature.workout.Workout;
import hr.algebra.FitConnect.feature.workout.WorkoutRepo;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExercise;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExerciseRepo;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseServiceTest {

    @Mock
    private WorkoutRepo workoutRepo;

    @Mock
    private ExerciseRepo exerciseRepo;

    @Mock
    private WorkoutExerciseRepo workoutExerciseRepo;

    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;

    private Workout workout;
    private Exercise exercise;
    private WorkoutExercise workoutExercise;

    @BeforeEach
    public void setUp() {
        workout = new Workout();
        workout.setWorkoutId(1);
        workout.setWorkoutName("Test Workout");
        workout.setDescription("Workout for testing");

        exercise = new Exercise();
        exercise.setExerciseId(1);
        exercise.setExerciseName("Push-Ups");
        exercise.setSets(3);
        exercise.setReps(12);

        workoutExercise = new WorkoutExercise();
        workoutExercise.setId(1);
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);
    }

    @Test
    public void testAddExerciseToWorkout_Success() {
        when(workoutRepo.findById(workout.getWorkoutId())).thenReturn(Optional.of(workout));
        when(exerciseRepo.findById(exercise.getExerciseId())).thenReturn(Optional.of(exercise));
        when(workoutRepo.save(any(Workout.class))).thenReturn(workout);

        Workout updatedWorkout = workoutExerciseService.addExerciseToWorkout(workout.getWorkoutId(), exercise.getExerciseId());

        assertNotNull(updatedWorkout);
        assertTrue(updatedWorkout.getExercises().contains(exercise));
        verify(workoutRepo, times(1)).findById(workout.getWorkoutId());
        verify(exerciseRepo, times(1)).findById(exercise.getExerciseId());
        verify(workoutRepo, times(1)).save(any(Workout.class));
    }

    @Test
    public void testRemoveExerciseFromWorkout_Success() {
        workout.addExercise(exercise); // Pre-associate exercise with workout

        when(workoutRepo.findById(workout.getWorkoutId())).thenReturn(Optional.of(workout));
        when(exerciseRepo.findById(exercise.getExerciseId())).thenReturn(Optional.of(exercise));
        when(workoutRepo.save(any(Workout.class))).thenReturn(workout);

        Workout updatedWorkout = workoutExerciseService.removeExerciseFromWorkout(workout.getWorkoutId(), exercise.getExerciseId());

        assertNotNull(updatedWorkout);
        assertFalse(updatedWorkout.getExercises().contains(exercise));
        verify(workoutRepo, times(1)).findById(workout.getWorkoutId());
        verify(exerciseRepo, times(1)).findById(exercise.getExerciseId());
        verify(workoutRepo, times(1)).save(any(Workout.class));
    }

    @Test
    public void testGetExercisesByWorkoutId_Success() {
        when(workoutExerciseRepo.findByWorkout_WorkoutId(workout.getWorkoutId()))
                .thenReturn(List.of(workoutExercise));

        List<WorkoutExercise> exercises = workoutExerciseService.getExercisesByWorkoutId(workout.getWorkoutId());

        assertNotNull(exercises);
        assertEquals(1, exercises.size());
        assertEquals(exercise.getExerciseName(), exercises.get(0).getExercise().getExerciseName());
        verify(workoutExerciseRepo, times(1)).findByWorkout_WorkoutId(workout.getWorkoutId());
    }


}

