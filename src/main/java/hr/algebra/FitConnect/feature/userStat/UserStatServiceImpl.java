package hr.algebra.FitConnect.feature.userStat;

import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserStatServiceImpl implements UserStatService {

    @Autowired
    private UserStatRepository userStatRepository;

    @Autowired
    private UserRepo userRepository;

    public UserStatServiceImpl(UserStatRepository userStatRepository, UserRepo userRepository) {
        this.userStatRepository = userStatRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserStat> getUserStats(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return userStatRepository.findByUserOrderByRecordedAtDesc(user);
    }

    public UserStat addUserStat(int userId, BigDecimal weight, BigDecimal height) {
        UserStat userStat = new UserStat();
        userStat.setUser(userRepository.findById(userId).orElseThrow());
        userStat.setWeight(weight);
        userStat.setHeight(height);
        userStat.setRecordedAt(LocalDateTime.now());
        return userStatRepository.save(userStat);
    }


}
