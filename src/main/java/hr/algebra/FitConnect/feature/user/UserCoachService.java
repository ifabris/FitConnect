package hr.algebra.FitConnect.feature.user;

import java.util.List;

public interface UserCoachService {
    void addUserCoach(int userId, int coachId);
    User getCoachForUser(int userId);
    List<User> getUsersForCoach(int coachId);
    boolean isCoach(int userId);
}
