package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import hr.algebra.FitConnect.feature.userStat.UserStat;
import hr.algebra.FitConnect.feature.userStat.UserStatRepository;
import hr.algebra.FitConnect.feature.userStat.UserStatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserStatServiceTest {

    @Mock
    private UserStatRepository userStatRepository;

    @Mock
    private UserRepo userRepository;

    @InjectMocks
    private UserStatServiceImpl userStatService;

    private User user;
    private UserStat userStat;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("testUser");

        userStat = new UserStat();
        userStat.setStat_id(1);
        userStat.setUser(user);
        userStat.setWeight(new BigDecimal("70.5"));
        userStat.setHeight(new BigDecimal("175"));
        userStat.setRecordedAt(LocalDateTime.now());
    }

    @Test
    public void testGetUserStats_Success() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userStatRepository.findByUserOrderByRecordedAtDesc(user))
                .thenReturn(List.of(userStat));

        List<UserStat> stats = userStatService.getUserStats(user.getUserId());

        assertNotNull(stats);
        assertEquals(1, stats.size());
        assertEquals(userStat.getWeight(), stats.get(0).getWeight());
        verify(userStatRepository, times(1)).findByUserOrderByRecordedAtDesc(user);
    }

    @Test
    public void testAddUserStat_Success() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userStatRepository.save(any(UserStat.class))).thenReturn(userStat);

        UserStat addedStat = userStatService.addUserStat(user.getUserId(),
                new BigDecimal("70.5"), new BigDecimal("175"));

        assertNotNull(addedStat);
        assertEquals(user.getUserId(), addedStat.getUser().getUserId());
        assertEquals(new BigDecimal("70.5"), addedStat.getWeight());
        assertEquals(new BigDecimal("175"), addedStat.getHeight());
        verify(userStatRepository, times(1)).save(any(UserStat.class));
    }

    @Test
    public void testDeleteStat_Success() {
        when(userStatRepository.existsById(userStat.getStat_id())).thenReturn(true);

        boolean result = userStatService.deleteStat(userStat.getStat_id());

        assertTrue(result);
        verify(userStatRepository, times(1)).deleteById(userStat.getStat_id());
    }

    @Test
    public void testDeleteStat_Failure() {
        when(userStatRepository.existsById(userStat.getStat_id())).thenReturn(false);

        boolean result = userStatService.deleteStat(userStat.getStat_id());

        assertFalse(result);
        verify(userStatRepository, never()).deleteById(userStat.getStat_id());
    }

}
