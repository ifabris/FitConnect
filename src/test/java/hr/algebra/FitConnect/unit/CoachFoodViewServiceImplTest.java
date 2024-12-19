package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.food.CoachFoodView;
import hr.algebra.FitConnect.feature.food.CoachFoodViewRepository;
import hr.algebra.FitConnect.feature.food.UserFoodLog;
import hr.algebra.FitConnect.feature.food.request.CoachFoodViewServiceImpl;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoachFoodViewServiceImplTest {

    @InjectMocks
    private CoachFoodViewServiceImpl coachFoodViewService;

    @Mock
    private CoachFoodViewRepository coachFoodViewRepo;

    @Mock
    private UserRepo userRepo;

    private User coach;
    private User user;
    private UserFoodLog userFoodLog;
    private CoachFoodView coachFoodView;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        coach = new User();
        coach.setUserId(1);
        coach.setUsername("coach1");

        user = new User();
        user.setUserId(2);
        user.setUsername("user1");

        userFoodLog = new UserFoodLog();
        userFoodLog.setLogId(100);
        userFoodLog.setUser(user);
        userFoodLog.setLogDate(LocalDate.now());

        coachFoodView = new CoachFoodView();
        coachFoodView.setId(1);
        coachFoodView.setCoach(coach);
        coachFoodView.setUser(user);
        coachFoodView.setUserFoodLog(userFoodLog);
    }

    @Test
    void saveCoachFoodView_ShouldSaveSuccessfully() {
        when(userRepo.findById(coach.getUserId())).thenReturn(Optional.of(coach));

        coachFoodViewService.saveCoachFoodView(coach.getUserId(), userFoodLog);

        verify(coachFoodViewRepo, times(1)).save(any(CoachFoodView.class));
    }

    @Test
    void saveCoachFoodView_ShouldThrowException_WhenCoachNotFound() {
        when(userRepo.findById(coach.getUserId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                coachFoodViewService.saveCoachFoodView(coach.getUserId(), userFoodLog));

        assertEquals("Coach not found with id: 1", exception.getMessage());
    }

    @Test
    void getFoodLogsByUserAndDate_ShouldReturnLogs() {
        when(coachFoodViewRepo.findByUser_UserIdAndUserFoodLog_LogDate(user.getUserId(), userFoodLog.getLogDate()))
                .thenReturn(Collections.singletonList(coachFoodView));

        List<UserFoodLog> logs = coachFoodViewService.getFoodLogsByUserAndDate(user.getUserId(), userFoodLog.getLogDate());

        assertNotNull(logs);
        assertEquals(1, logs.size());
        assertEquals(userFoodLog, logs.get(0));
    }

    @Test
    void getFoodLogsByUserAndDate_ShouldReturnEmptyList_WhenNoLogsFound() {
        when(coachFoodViewRepo.findByUser_UserIdAndUserFoodLog_LogDate(user.getUserId(), userFoodLog.getLogDate()))
                .thenReturn(Collections.emptyList());

        List<UserFoodLog> logs = coachFoodViewService.getFoodLogsByUserAndDate(user.getUserId(), userFoodLog.getLogDate());

        assertNotNull(logs);
        assertTrue(logs.isEmpty());
    }
}

