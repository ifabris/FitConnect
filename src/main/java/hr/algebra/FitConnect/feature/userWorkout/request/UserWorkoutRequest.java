package hr.algebra.FitConnect.feature.userWorkout.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserWorkoutRequest {
    private int userId;
    private int workoutId;
    private LocalDate workoutDate;

}