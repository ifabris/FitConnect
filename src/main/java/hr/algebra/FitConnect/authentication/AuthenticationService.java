package hr.algebra.FitConnect.authentication;

import hr.algebra.FitConnect.authentication.jwt.JwtService;
import hr.algebra.FitConnect.feature.user.*;
import hr.algebra.FitConnect.feature.user.request.LoginRequest;
import hr.algebra.FitConnect.feature.user.request.RegisterRequest;
import hr.algebra.FitConnect.feature.user.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepo roleRepo;

    public LoginResponse login(LoginRequest request) {
        // Find the user by username
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        // Check if the password matches
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
        }

        // Determine the user's role
        RoleType userRole = determineUserRole(user);

        // Generate the JWT token
        String jwtToken = jwtService.createJwt(user);

        // Return the LoginResponse with the JWT token, role, and user ID
        return new LoginResponse(jwtToken, userRole, user.getUserId());
    }


    private RoleType determineUserRole(User user) {
        Role userRole = user.getRole();  // Assuming User has a single Role (not a Set)

        if (userRole == null || userRole.getRoleName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User role not assigned");
        }

        // Assuming getRoleName() already returns a RoleType
        return userRole.getRoleName();  // Directly return the RoleType
    }





    public void register(RegisterRequest request) {
        try {
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));

            // Fetch role from the roles table based on the role passed in the request
            Role role = roleRepo.findByRoleName(request.getRole())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role"));
            newUser.setRole(role);

            LocalDateTime now = LocalDateTime.now();
            newUser.setCreatedAt(now);
            newUser.setUpdatedAt(now);

            userRepo.save(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }




    public void logout(String token){
        jwtService.invalidateToken(token);
    }
}