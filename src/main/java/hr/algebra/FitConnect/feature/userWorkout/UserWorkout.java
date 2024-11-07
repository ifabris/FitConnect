package hr.algebra.FitConnect.feature.userWorkout;

import hr.algebra.FitConnect.feature.workout.Workout;
import hr.algebra.FitConnect.feature.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_workouts")
public class UserWorkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userWorkoutId;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate workoutDate;

    // Getters and setters...
}

