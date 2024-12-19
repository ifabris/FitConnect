package hr.algebra.FitConnect.feature.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId; // Auto-incremented user ID
    @Column(name="username")
    private String username; // Username
    @Column(name="password")
    private String password; // Password (hashed)
    @Column(name="email")
    private String email; // Email address
    @Column(name="picture")
    private String picture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)  // Foreign key to Roles table
    private Role role;

    @Column(name="createdAt")
    private LocalDateTime createdAt; // Timestamp when user was created
    @Column(name="updatedAt")
    private LocalDateTime updatedAt; // Timestamp for last update

    public User(Integer userId, String username, String password, String email, Role role, String picture, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.picture = picture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Default constructor for JPA
    public User() {
    }

}


