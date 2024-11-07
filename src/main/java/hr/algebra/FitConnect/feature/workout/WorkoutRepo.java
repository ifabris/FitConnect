package hr.algebra.FitConnect.feature.workout;

import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.workout.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepo extends JpaRepository<Workout, Integer> {
    List<Workout> findByCoach(User coach); // This should work now
}

