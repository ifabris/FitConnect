package hr.algebra.FitConnect.feature.user.response;

import hr.algebra.FitConnect.feature.user.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginResponse {

    private final String jwtToken;
    private final RoleType role;
    private final int userId;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "jwtToken='" + jwtToken + '\'' +
                ", role=" + role +
                ", userId=" + userId +
                '}';
    }
}
