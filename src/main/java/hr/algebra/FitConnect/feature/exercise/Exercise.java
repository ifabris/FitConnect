package hr.algebra.FitConnect.feature.exercise;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hr.algebra.FitConnect.feature.workout.Workout;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exerciseId;

    private String exerciseName;
    private int sets;
    private int reps;

    @ManyToMany(mappedBy = "exercises")
    @JsonBackReference
    private List<Workout> workouts = new ArrayList<>();
}

