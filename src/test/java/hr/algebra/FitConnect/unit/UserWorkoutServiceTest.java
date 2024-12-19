package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import hr.algebra.FitConnect.feature.userWorkout.UserWorkout;
import hr.algebra.FitConnect.feature.userWorkout.UserWorkoutRepo;
import hr.algebra.FitConnect.feature.userWorkout.UserWorkoutService;
import hr.algebra.FitConnect.feature.workout.Workout;
import hr.algebra.FitConnect.feature.workout.WorkoutRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserWorkoutServiceTest {

    @Mock
    private UserWorkoutRepo userWorkoutRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private WorkoutRepo workoutRepo;

    @InjectMocks
    private UserWorkoutService userWorkoutService;

    private User user;
    private Workout workout;
    private UserWorkout userWorkout;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("testUser");

        workout = new Workout();
        workout.setWorkoutId(1);
        workout.setWorkoutName("Test Workout");

        userWorkout = new UserWorkout();
        userWorkout.setUserWorkoutId(1);
        userWorkout.setUser(user);
        userWorkout.setWorkout(workout);
        userWorkout.setWorkoutDate(LocalDate.now());
    }

    @Test
    public void testCreateUserWorkout_Success() {
        when(userWorkoutRepo.save(any(UserWorkout.class))).thenReturn(userWorkout);

        UserWorkout savedWorkout = userWorkoutService.createUserWorkout(userWorkout);

        assertNotNull(savedWorkout);
        assertEquals(user.getUserId(), savedWorkout.getUser().getUserId());
        assertEquals(workout.getWorkoutId(), savedWorkout.getWorkout().getWorkoutId());
        verify(userWorkoutRepo, times(1)).save(any(UserWorkout.class));
    }

    @Test
    public void testFindWorkoutsForUser_Success() {
        when(userWorkoutRepo.findByUser(user)).thenReturn(List.of(userWorkout));

        List<UserWorkout> userWorkouts = userWorkoutRepo.findByUser(user);

        assertNotNull(userWorkouts);
        assertEquals(1, userWorkouts.size());
        assertEquals(userWorkout.getUserWorkoutId(), userWorkouts.get(0).getUserWorkoutId());
        verify(userWorkoutRepo, times(1)).findByUser(user);
    }

    @Test
    public void testFindWorkoutsByUserIdAndDate_Success() {
        when(userWorkoutRepo.findByUserIdAndWorkoutDate(user.getUserId(), LocalDate.now()))
                .thenReturn(List.of(workout));

        List<Workout> workouts = userWorkoutRepo.findByUserIdAndWorkoutDate(user.getUserId(), LocalDate.now());

        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals(workout.getWorkoutId(), workouts.get(0).getWorkoutId());
        verify(userWorkoutRepo, times(1))
                .findByUserIdAndWorkoutDate(user.getUserId(), LocalDate.now());
    }


}

