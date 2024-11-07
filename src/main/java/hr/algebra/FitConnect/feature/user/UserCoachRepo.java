package hr.algebra.FitConnect.feature.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserCoachRepo extends JpaRepository<UserCoach, UserCoachId> {
    Optional<UserCoach> findByUser_UserId(int userId); // Adjusted method name

    @Query("SELECT uc.user FROM UserCoach uc WHERE uc.coach.userId = :coachId")
    List<User> findUsersByCoachId(int coachId);

}
