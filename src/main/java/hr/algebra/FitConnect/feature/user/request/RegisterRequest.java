package hr.algebra.FitConnect.feature.user.request;

import hr.algebra.FitConnect.feature.user.RoleType;
import lombok.Data;

@Data
public class RegisterRequest {

    private String username;

    private String email;

    private String password;

    private RoleType role;
}
