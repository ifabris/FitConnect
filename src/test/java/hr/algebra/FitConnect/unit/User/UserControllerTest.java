package hr.algebra.FitConnect.unit.User;

import hr.algebra.FitConnect.feature.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<User> mockUsers;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock data
        Role adminRole = new Role(1, RoleType.ADMIN);
        User user1 = new User(1, "admin", "password", "admin@example.com", adminRole, null, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(2, "user", "password", "user@example.com", adminRole, null, LocalDateTime.now(), LocalDateTime.now());

        mockUsers = new ArrayList<>();
        mockUsers.add(user1);
        mockUsers.add(user2);
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        Authentication mockAuthentication = mock(Authentication.class);
        User mockAdmin = new User();
        mockAdmin.setRole(new Role(1, RoleType.ADMIN)); // Assuming Role is a class with id and name
        when(mockAuthentication.getPrincipal()).thenReturn(mockAdmin);

        when(userService.getAllUsers()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers(mockAuthentication);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }


    @Test
    public void testGetUserById() {
        // Arrange
        User mockUser = mockUsers.get(0);
        when(userService.getUserById(1)).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = userController.getUserById(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("admin", response.getBody().getUsername());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    public void testGetUserById_NotFound() {
        // Arrange
        when(userService.getUserById(99)).thenReturn(null);

        // Act
        ResponseEntity<User> response = userController.getUserById(99);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserById(99);
    }

    @Test
    public void testCreateUser() {
        // Arrange
        Role regularRole = new Role(2, RoleType.REGULAR_USER);
        User newUser = new User(null, "newUser", "password", "newuser@example.com", regularRole, null, LocalDateTime.now(), null);
        User savedUser = new User(3, "newUser", "password", "newuser@example.com", regularRole, null, LocalDateTime.now(), LocalDateTime.now());
        when(userService.saveUser(newUser)).thenReturn(savedUser);

        // Act
        ResponseEntity<User> response = userController.createUser(newUser);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getUserId());
        verify(userService, times(1)).saveUser(newUser);
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        when(userService.deleteUser(1)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(1);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    public void testDeleteUser_NotFound() {
        // Arrange
        when(userService.deleteUser(99)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(99);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(99);
    }
}

