package hr.algebra.FitConnect.feature.userStat;

import hr.algebra.FitConnect.feature.userStat.request.UserStatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/userStats")
public class UserStatController {
    @Autowired
    private UserStatService userStatService;

    public UserStatController(UserStatService userStatService) {
        this.userStatService = userStatService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserStat>> getUserStats(@PathVariable int userId) {
        return ResponseEntity.ok(userStatService.getUserStats(userId));
    }


    @PostMapping("/{userId}")
    public ResponseEntity<UserStat> addUserStat(
            @PathVariable int userId,
            @RequestBody UserStatRequest request) {
        UserStat stat = userStatService.addUserStat(userId, request.getWeight(), request.getHeight());
        return ResponseEntity.status(HttpStatus.CREATED).body(stat);
    }

    @DeleteMapping("/{statId}")
    public ResponseEntity<Void> deleteUserStat(@PathVariable int statId) {
        userStatService.deleteStat(statId);
        return ResponseEntity.noContent().build();
    }

}
