package hr.algebra.FitConnect.feature.workoutExercise;

import hr.algebra.FitConnect.feature.workoutExercise.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutExerciseRepo extends JpaRepository<WorkoutExercise, Integer> {

    List<WorkoutExercise> findByWorkout_WorkoutId(int workoutId);

}

