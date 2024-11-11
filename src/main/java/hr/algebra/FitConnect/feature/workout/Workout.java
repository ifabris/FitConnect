package hr.algebra.FitConnect.feature.workout;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hr.algebra.FitConnect.feature.exercise.Exercise;
import hr.algebra.FitConnect.feature.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "workouts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "workoutId")
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workoutId;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    private String workoutName;
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "workout_exercises",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<Exercise> exercises = new ArrayList<>();

    // Convenience methods for managing exercises
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        exercise.getWorkouts().add(this);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
        exercise.getWorkouts().remove(this);
    }
}
