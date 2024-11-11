package hr.algebra.FitConnect.feature.userWorkout;

import hr.algebra.FitConnect.feature.userWorkout.UserWorkout;
import hr.algebra.FitConnect.feature.userWorkout.UserWorkoutRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserWorkoutService {

    @Autowired
    private UserWorkoutRepo userWorkoutRepo;

    public UserWorkout createUserWorkout(UserWorkout userWorkout) {
        return userWorkoutRepo.save(userWorkout);
    }
}

