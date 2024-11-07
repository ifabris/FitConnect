package hr.algebra.FitConnect.feature.user;


import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    User saveUser(User user);
    User updateUser(int id, User user);
    boolean deleteUser(int id);
    List<User> getAllCoaches();
}
