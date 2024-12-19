package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.exercise.DTO.WorkoutDTO;
import hr.algebra.FitConnect.feature.exercise.Exercise;
import hr.algebra.FitConnect.feature.exercise.ExerciseRepo;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.userWorkout.UserWorkoutRepo;
import hr.algebra.FitConnect.feature.workout.Workout;
import hr.algebra.FitConnect.feature.workout.WorkoutRepo;
import hr.algebra.FitConnect.feature.workout.WorkoutService;
import hr.algebra.FitConnect.feature.workout.request.WorkoutRequestDTO;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExercise;
import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExerciseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @Mock
    private WorkoutRepo workoutRepo;

    @Mock
    private ExerciseRepo exerciseRepo;

    @Mock
    private WorkoutExerciseRepo workoutExerciseRepo;

    @Mock
    private UserWorkoutRepo userWorkoutRepo;

    @InjectMocks
    private WorkoutService workoutService;

    private Workout workout;
    private Exercise exercise;
    private User coach;

    @BeforeEach
    public void setUp() {
        coach = new User();
        coach.setUserId(1);
        coach.setUsername("coachUser");

        workout = new Workout();
        workout.setWorkoutId(1);
        workout.setWorkoutName("Test Workout");
        workout.setDescription("This is a test workout");
        workout.setCoach(coach);
        workout.setCreatedAt(LocalDateTime.now());

        exercise = new Exercise();
        exercise.setExerciseId(1);
        exercise.setExerciseName("Push-Ups");
        exercise.setSets(3);
        exercise.setReps(10);

        workout.addExercise(exercise);
    }

    @Test
    public void testCreateWorkout_Success() {
        WorkoutRequestDTO workoutRequest = new WorkoutRequestDTO();
        workoutRequest.setWorkoutName("Test Workout");
        workoutRequest.setDescription("A workout for testing");
        workoutRequest.setExerciseIds(List.of(1));

        when(workoutRepo.save(any(Workout.class))).thenReturn(workout);
        when(exerciseRepo.findById(1)).thenReturn(Optional.of(exercise));
        when(workoutExerciseRepo.save(any(WorkoutExercise.class))).thenReturn(new WorkoutExercise());

        WorkoutDTO createdWorkout = workoutService.createWorkout(workoutRequest, coach);

        assertNotNull(createdWorkout);
        assertEquals("Test Workout", createdWorkout.getWorkoutName());
        assertEquals(1, createdWorkout.getExercises().size());
        verify(workoutRepo, times(1)).save(any(Workout.class));
        verify(workoutExerciseRepo, times(1)).save(any(WorkoutExercise.class));
    }

    @Test
    public void testAddExerciseToWorkout_Success() {
        when(workoutRepo.findById(workout.getWorkoutId())).thenReturn(Optional.of(workout));
        when(exerciseRepo.save(any(Exercise.class))).thenReturn(exercise);
        when(workoutExerciseRepo.save(any(WorkoutExercise.class))).thenReturn(new WorkoutExercise());

        Exercise addedExercise = workoutService.addExerciseToWorkout(workout.getWorkoutId(), exercise);

        assertNotNull(addedExercise);
        assertEquals("Push-Ups", addedExercise.getExerciseName());
        verify(workoutRepo, times(1)).findById(workout.getWorkoutId());
        verify(exerciseRepo, times(1)).save(any(Exercise.class));
        verify(workoutExerciseRepo, times(1)).save(any(WorkoutExercise.class));
    }

    @Test
    public void testGetWorkoutsByUserAndDate_Success() {
        when(userWorkoutRepo.findByUserIdAndWorkoutDate(coach.getUserId(), LocalDate.now()))
                .thenReturn(List.of(workout));

        List<Workout> workouts = workoutService.getWorkoutsByUserAndDate(coach.getUserId(), LocalDate.now());

        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals("Test Workout", workouts.get(0).getWorkoutName());
        verify(userWorkoutRepo, times(1))
                .findByUserIdAndWorkoutDate(coach.getUserId(), LocalDate.now());
    }
    @Test
    public void testFindWorkoutById_Success() {
        when(workoutRepo.findById(workout.getWorkoutId())).thenReturn(Optional.of(workout));

        Workout foundWorkout = workoutService.findById(workout.getWorkoutId());

        assertNotNull(foundWorkout);
        assertEquals("Test Workout", foundWorkout.getWorkoutName());
        verify(workoutRepo, times(1)).findById(workout.getWorkoutId());
    }


}

