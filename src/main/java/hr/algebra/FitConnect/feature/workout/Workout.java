package hr.algebra.FitConnect.feature.workout;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workoutId;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false) // Make sure this is present
    private User coach; // This field represents the coach for the workout

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
    @JsonManagedReference
    private List<Exercise> exercises = new ArrayList<>();

    // Constructors, getters, and setters

    // Add convenience methods for managing the exercises
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        exercise.getWorkouts().add(this); // Also add this workout to the exercise
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
        exercise.getWorkouts().remove(this); // Also remove this workout from the exercise
    }
}
