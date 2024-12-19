package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserCoachServiceTest {

    @Mock
    private UserCoachRepo userCoachRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserCoachServiceImpl userCoachService;

    private User user;
    private User coach;
    private UserCoach userCoach;
    private UserCoachId userCoachId;

    @BeforeEach
    public void setUp() {
        Role coachRole = new Role(2, RoleType.COACH);
        Role userRole = new Role(3, RoleType.REGULAR_USER);

        coach = new User(1, "coach", "pass123", "coach@example.com", coachRole, null, LocalDateTime.now(), LocalDateTime.now());
        user = new User(2, "user", "pass123", "user@example.com", userRole, null, LocalDateTime.now(), LocalDateTime.now());

        userCoachId = new UserCoachId(user.getUserId(), coach.getUserId());
        userCoach = new UserCoach();
        userCoach.setId(userCoachId);
        userCoach.setUser(user);
        userCoach.setCoach(coach);
    }

    @Test
    public void testAddUserCoach_Success() {
        when(userRepo.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepo.findById(coach.getUserId())).thenReturn(Optional.of(coach));
        when(userCoachRepo.save(any(UserCoach.class))).thenReturn(userCoach);

        userCoachService.addUserCoach(user.getUserId(), coach.getUserId());

        verify(userCoachRepo, times(1)).save(any(UserCoach.class));
    }

    @Test
    public void testGetCoachForUser_Success() {
        when(userCoachRepo.findByUser_UserId(user.getUserId())).thenReturn(Optional.of(userCoach));

        User retrievedCoach = userCoachService.getCoachForUser(user.getUserId());

        assertNotNull(retrievedCoach);
        assertEquals(coach.getUserId(), retrievedCoach.getUserId());
        assertEquals(coach.getUsername(), retrievedCoach.getUsername());
    }

    @Test
    public void testGetUsersForCoach_Success() {
        List<User> users = List.of(user);
        when(userCoachRepo.findUsersByCoachId(coach.getUserId())).thenReturn(users);

        List<User> retrievedUsers = userCoachService.getUsersForCoach(coach.getUserId());

        assertNotNull(retrievedUsers);
        assertEquals(1, retrievedUsers.size());
        assertEquals(user.getUserId(), retrievedUsers.get(0).getUserId());
    }

    @Test
    public void testIsCoach_Success() {
        when(userRepo.findById(coach.getUserId())).thenReturn(Optional.of(coach));

        boolean isCoach = userCoachService.isCoach(coach.getUserId());

        assertTrue(isCoach);
    }

    @Test
    public void testIsCoach_Failure() {
        when(userRepo.findById(user.getUserId())).thenReturn(Optional.of(user));

        boolean isCoach = userCoachService.isCoach(user.getUserId());

        assertFalse(isCoach);
    }

}

