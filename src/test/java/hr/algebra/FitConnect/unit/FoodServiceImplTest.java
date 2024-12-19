package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.food.*;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodServiceImplTest {

    @Mock
    private FoodRepository foodRepo;

    @Mock
    private UserFoodLogRepository userFoodLogRepo;

    @Mock
    private CoachFoodViewRepository coachFoodViewRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private FoodServiceImpl foodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFood_shouldSaveAndReturnFood() {
        Food food = new Food();
        food.setFoodName("Apple");
        food.setCalories(95);

        when(foodRepo.save(any(Food.class))).thenReturn(food);

        Food savedFood = foodService.addFood(food);

        assertEquals("Apple", savedFood.getFoodName());
        assertEquals(95, savedFood.getCalories());
        verify(foodRepo, times(1)).save(food);
    }

    @Test
    void getAllFoods_shouldReturnListOfFoods() {
        Food food1 = new Food();
        food1.setFoodName("Apple");

        Food food2 = new Food();
        food2.setFoodName("Banana");

        when(foodRepo.findAll()).thenReturn(Arrays.asList(food1, food2));

        List<Food> foods = foodService.getAllFoods();

        assertEquals(2, foods.size());
        assertEquals("Apple", foods.get(0).getFoodName());
        verify(foodRepo, times(1)).findAll();
    }

    @Test
    void getFoodById_shouldReturnFood() {
        Food food = new Food();
        food.setFoodId(1);
        food.setFoodName("Apple");

        when(foodRepo.findById(1)).thenReturn(Optional.of(food));

        Food foundFood = foodService.getFoodById(1);

        assertEquals("Apple", foundFood.getFoodName());
        verify(foodRepo, times(1)).findById(1);
    }

    @Test
    void getFoodById_shouldThrowExceptionIfFoodNotFound() {
        when(foodRepo.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> foodService.getFoodById(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Food not found\"", exception.getMessage());
        verify(foodRepo, times(1)).findById(1);
    }

    @Test
    void updateFood_shouldUpdateAndReturnFood() {
        Food existingFood = new Food();
        existingFood.setFoodId(1);
        existingFood.setFoodName("Apple");

        Food updatedDetails = new Food();
        updatedDetails.setFoodName("Banana");
        updatedDetails.setCalories(105);

        when(foodRepo.findById(1)).thenReturn(Optional.of(existingFood));
        when(foodRepo.save(any(Food.class))).thenReturn(updatedDetails);

        Food updatedFood = foodService.updateFood(1, updatedDetails);

        assertEquals("Banana", updatedFood.getFoodName());
        assertEquals(105, updatedFood.getCalories());
        verify(foodRepo, times(1)).save(existingFood);
    }

    @Test
    void updateFood_shouldThrowExceptionIfFoodNotFound() {
        Food updatedDetails = new Food();
        updatedDetails.setFoodName("Banana");

        when(foodRepo.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> foodService.updateFood(1, updatedDetails));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Food not found\"", exception.getMessage());
        verify(foodRepo, never()).save(any(Food.class));
    }

    @Test
    void deleteFood_shouldRemoveFood() {
        Food food = new Food();
        food.setFoodId(1);

        when(foodRepo.findById(1)).thenReturn(Optional.of(food));

        foodService.deleteFood(1);

        verify(foodRepo, times(1)).delete(food);
    }

    @Test
    void deleteFood_shouldThrowExceptionIfFoodNotFound() {
        when(foodRepo.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> foodService.deleteFood(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Food not found\"", exception.getMessage());
        verify(foodRepo, never()).delete(any(Food.class));
    }
}
