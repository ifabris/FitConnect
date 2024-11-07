package hr.algebra.FitConnect.authentication;

import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserService;
import hr.algebra.FitConnect.feature.user.request.LoginRequest;
import hr.algebra.FitConnect.feature.user.request.RegisterRequest;
import hr.algebra.FitConnect.feature.user.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
    }

    @GetMapping("/logout")
    public void logout(@RequestHeader("Authorization") String bearer) {
        authenticationService.logout(bearer.substring(7));
    }
}
