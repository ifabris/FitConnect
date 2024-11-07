package hr.algebra.FitConnect.feature.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCoachServiceImpl implements UserCoachService {
    @Autowired
    private UserCoachRepo userCoachRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void addUserCoach(int userId, int coachId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User coach = userRepo.findById(coachId).orElseThrow(() -> new RuntimeException("Coach not found"));

        UserCoach userCoach = new UserCoach();
        userCoach.setUser(user);
        userCoach.setCoach(coach);
        userCoachRepo.save(userCoach);
    }

    @Override
    public User getCoachForUser(int userId) {
        // Find the UserCoach entity for the given userId
        Optional<UserCoach> userCoachOptional = userCoachRepo.findByUser_UserId(userId); // Ensure method name reflects the correct query

        // Check if the UserCoach entity is present
        if (userCoachOptional.isPresent()) {
            UserCoach userCoach = userCoachOptional.get();
            return userCoach.getCoach(); // Directly return the coach, which is already a User object
        }

        return null; // Return null if no coach is found
    }

    @Override
    public List<User> getUsersForCoach(int coachId) {
        return userCoachRepo.findUsersByCoachId(coachId);
    }

    @Override
    public boolean isCoach(int userId) {
        Optional<User> user = userRepo.findById(userId);
        return user.isPresent() && user.get().getRole().getRoleId() == 2;
    }
}
