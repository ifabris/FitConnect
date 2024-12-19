package hr.algebra.FitConnect.feature.user;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType roleName;

    // Add a constructor with all fields
    public Role(int roleId, RoleType roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    // Default constructor for JPA
    public Role() {
    }
}

