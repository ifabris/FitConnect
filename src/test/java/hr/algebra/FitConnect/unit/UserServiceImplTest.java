package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> mockUsers = List.of(
                new User(1, "user1", "pass1", "user1@example.com", null, null, null, null),
                new User(2, "user2", "pass2", "user2@example.com", null, null, null, null)
        );
        Mockito.when(userRepo.findAll()).thenReturn(mockUsers);

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
    }

    @Test
    void testGetUserById() {
        // Arrange
        User mockUser = new User(1, "user1", "pass1", "user1@example.com", null, null, null, null);
        Mockito.when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));

        // Act
        User user = userService.getUserById(1);

        // Assert
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        Mockito.when(userRepo.findById(1)).thenReturn(Optional.empty());

        // Act
        User user = userService.getUserById(1);

        // Assert
        assertNull(user);
    }

    @Test
    void testSaveUser() {
        // Arrange
        User mockUser = new User(null, "user1", "pass1", "user1@example.com", null, null, null, null);
        User savedUser = new User(1, "user1", "pass1", "user1@example.com", null, null, null, null);
        Mockito.when(userRepo.save(mockUser)).thenReturn(savedUser);

        // Act
        User result = userService.saveUser(mockUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getUserId());
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User existingUser = new User(1, "user1", "pass1", "user1@example.com", null, null, null, null);
        User updatedDetails = new User(null, "updatedUser", null, null, null, null, null, null);
        Mockito.when(userRepo.findById(1)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateUser(1, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
    }

    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        Mockito.when(userRepo.findById(1)).thenReturn(Optional.empty());

        // Act
        User result = userService.updateUser(1, new User());

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Mockito.when(userRepo.existsById(1)).thenReturn(true);

        // Act
        boolean deleted = userService.deleteUser(1);

        // Assert
        assertTrue(deleted);
        Mockito.verify(userRepo, Mockito.times(1)).deleteById(1);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        Mockito.when(userRepo.existsById(1)).thenReturn(false);

        // Act
        boolean deleted = userService.deleteUser(1);

        // Assert
        assertFalse(deleted);
    }

    @Test
    void testGetAllCoaches() {
        // Arrange
        Role coachRole = new Role(2, RoleType.COACH);
        User coach = new User(1, "coach1", "pass1", "coach@example.com", coachRole, null, null, null);
        Mockito.when(userRepo.findByRoleRoleName(RoleType.COACH)).thenReturn(List.of(coach));

        // Act
        List<User> coaches = userService.getAllCoaches();

        // Assert
        assertEquals(1, coaches.size());
        assertEquals("coach1", coaches.get(0).getUsername());
    }
}
