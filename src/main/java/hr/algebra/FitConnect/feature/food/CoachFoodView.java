package hr.algebra.FitConnect.feature.food;

import hr.algebra.FitConnect.feature.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "coach_food_view")
public class CoachFoodView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Single primary key

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "log_id", nullable = false)
    private UserFoodLog userFoodLog;

}
