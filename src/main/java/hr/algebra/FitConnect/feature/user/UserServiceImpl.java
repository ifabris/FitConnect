package hr.algebra.FitConnect.feature.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;

    @Autowired
    public UserServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @Override
    public User getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);  // Return null if user not found
    }

    // Save new user
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Update user by ID
    @Override
    public User updateUser(int id, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Update only non-null fields
            if (updatedUser.getUsername() != null) {
                existingUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {  // Only update if a new password is provided
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getRole() != null) {  // Only update if a new role is provided
                existingUser.setRole(updatedUser.getRole());
            }

            // Update the timestamp
            existingUser.setUpdatedAt(LocalDateTime.now());

            return userRepository.save(existingUser);  // Save the updated user
        }

        return null;  // Return null if the user does not exist
    }

    @Override
    public List<User> getAllCoaches() {
        return userRepository.findByRoleRoleName(RoleType.COACH);
    }



    // Delete user by ID
    @Override
    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;  // Successfully deleted
        }
        return false;  // User does not exist
    }
}
