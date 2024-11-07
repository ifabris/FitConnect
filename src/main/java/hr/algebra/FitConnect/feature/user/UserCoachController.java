package hr.algebra.FitConnect.feature.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-coach")
public class UserCoachController {

    private final UserCoachService userCoachService;

    @Autowired
    private UserCoachRepo userCoachRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    public UserCoachController(UserCoachService userCoachService) {
        this.userCoachService = userCoachService;
    }

    @GetMapping("/coach")
    public ResponseEntity<User> getCoachForUser(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        int userId = currentUser.getUserId(); // Get userId from the authenticated user

        User coach = userCoachService.getCoachForUser(userId);
        if (coach == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(coach);
    }
    @PostMapping("/add-coach/{coachId}")
    public ResponseEntity<String> addCoachToUser(@PathVariable int coachId, Authentication authentication) {
        // Fetch the user and coach from the database
        User user = (User) authentication.getPrincipal();
        User coach = userRepo.findById(coachId).orElse(null);

        if (user == null || coach == null) {
            return ResponseEntity.badRequest().body("User or coach not found.");
        }

        // Create UserCoach composite key
        UserCoachId userCoachId = new UserCoachId(user.getUserId(), coach.getUserId());

        // Create UserCoach entity
        UserCoach userCoach = new UserCoach();
        userCoach.setId(userCoachId);
        userCoach.setUser(user);
        userCoach.setCoach(coach);

        // Save to repository
        userCoachRepo.save(userCoach);

        return ResponseEntity.ok("Coach assigned successfully.");
    }

    @GetMapping("/coach/users")
    public ResponseEntity<?> getUsersForCoach(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        // Check if the authenticated user exists and is a coach
        if (!userCoachService.isCoach(user.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only coaches can access this resource.");
        }

        // Fetch users for the coach if the check passed
        List<User> users = userCoachService.getUsersForCoach(user.getUserId());
        return ResponseEntity.ok(users);
    }
}

