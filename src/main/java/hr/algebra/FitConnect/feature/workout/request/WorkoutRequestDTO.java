package hr.algebra.FitConnect.feature.workout.request;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutRequestDTO {
    private String workoutName;
    private String description;
    private List<Integer> exerciseIds; // List of exercise IDs to add to the workout

}

