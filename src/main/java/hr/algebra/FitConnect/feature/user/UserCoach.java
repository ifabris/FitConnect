package hr.algebra.FitConnect.feature.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_coach")
public class UserCoach {
    @EmbeddedId
    private UserCoachId id;

    @ManyToOne
    @MapsId("userId") // Specifies that this field maps to the userId part of the composite key
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("coachId") // Specifies that this field maps to the coachId part of the composite key
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    // Getters and Setters
}
