package hr.algebra.FitConnect.feature.food;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int foodId;

    private String foodName;
    private int calories;
    private int protein;
    private int carbs;
    private int fat;
}
