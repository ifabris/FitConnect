package hr.algebra.FitConnect.feature.userStat;

import hr.algebra.FitConnect.feature.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStatRepository extends JpaRepository<UserStat, Integer> {
    List<UserStat> findByUserOrderByRecordedAtDesc(User user);
}