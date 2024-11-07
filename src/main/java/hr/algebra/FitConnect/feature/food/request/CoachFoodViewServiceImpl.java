package hr.algebra.FitConnect.feature.food.request;

import hr.algebra.FitConnect.feature.food.CoachFoodView;
import hr.algebra.FitConnect.feature.food.CoachFoodViewRepository;
import hr.algebra.FitConnect.feature.food.CoachFoodViewService;
import hr.algebra.FitConnect.feature.food.UserFoodLog;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachFoodViewServiceImpl implements CoachFoodViewService {

    @Autowired
    private CoachFoodViewRepository coachFoodViewRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void saveCoachFoodView(int coachId, UserFoodLog userFoodLog) {

        User coach = userRepo.findById(coachId)
                .orElseThrow(() -> new RuntimeException("Coach not found with id: " + coachId)); // Handle the case when the coach is not found

        CoachFoodView coachFoodView = new CoachFoodView();
        coachFoodView.setCoach(coach);
        coachFoodView.setUser(userFoodLog.getUser());
        coachFoodView.setUserFoodLog(userFoodLog);
        coachFoodViewRepo.save(coachFoodView);
    }

    @Override
    public List<UserFoodLog> getFoodLogsByUserAndDate(int userId, LocalDate date) {
        // Fetch the list of CoachFoodView objects
        List<CoachFoodView> coachFoodViews = coachFoodViewRepo.findByUser_UserIdAndUserFoodLog_LogDate(userId, date);

        // Extract the UserFoodLog from each CoachFoodView and return the list
        return coachFoodViews.stream()
                .map(CoachFoodView::getUserFoodLog) // Extracts the UserFoodLog from each CoachFoodView
                .collect(Collectors.toList());      // Collects them into a List<UserFoodLog>
    }

}

