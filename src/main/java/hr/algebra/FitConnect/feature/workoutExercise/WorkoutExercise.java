package hr.algebra.FitConnect.feature.workoutExercise;

import hr.algebra.FitConnect.feature.exercise.Exercise;
import hr.algebra.FitConnect.feature.workout.Workout;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "workout_exercises")
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Primary key

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout; // Reference to the Workout

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise; // Reference to the Exercise

}

