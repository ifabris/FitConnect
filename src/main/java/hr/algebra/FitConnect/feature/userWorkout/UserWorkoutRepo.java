package hr.algebra.FitConnect.feature.userWorkout;

import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.userWorkout.UserWorkout;
import hr.algebra.FitConnect.feature.workout.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserWorkoutRepo extends JpaRepository<UserWorkout, Integer> {
    List<UserWorkout> findByUser(User user);

    @Query("SELECT uw.workout FROM UserWorkout uw " +
            "WHERE uw.user.userId = :userId " +
            "AND uw.workoutDate = :workoutDate")
    List<Workout> findByUserIdAndWorkoutDate(
            @Param("userId") int userId,
            @Param("workoutDate") LocalDate workoutDate);


}
