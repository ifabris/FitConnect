package hr.algebra.FitConnect.feature.userStat;


import hr.algebra.FitConnect.feature.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserStatService {

    List<UserStat> getUserStats(int userId);

    UserStat addUserStat(int userId, BigDecimal weight, BigDecimal height);

}
