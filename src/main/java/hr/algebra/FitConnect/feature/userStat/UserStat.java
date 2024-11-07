package hr.algebra.FitConnect.feature.userStat;

import hr.algebra.FitConnect.feature.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_stats")
public class UserStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stat_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private BigDecimal weight;
    private BigDecimal height;

    @Column(name = "bmi", insertable = false, updatable = false)
    private Double bmi;


    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;

    // Getters and setters
}
