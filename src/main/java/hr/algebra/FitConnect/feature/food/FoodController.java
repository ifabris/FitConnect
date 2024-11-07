package hr.algebra.FitConnect.feature.food;

import hr.algebra.FitConnect.feature.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping
    public ResponseEntity<Food> addFood(@RequestBody Food food, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();

        if (admin.getRole().getRoleId() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can add food.");
        }

        Food newFood = foodService.addFood(food);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFood);
    }

    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        return ResponseEntity.ok(foodService.getAllFoods());
    }

    // Get food by ID
    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable int id) {
        Food food = foodService.getFoodById(id);
        return ResponseEntity.ok(food);
    }

    // Update food by ID
    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable int id, @RequestBody Food foodDetails, Authentication authentication) {

        User admin = (User) authentication.getPrincipal();

        if (admin.getRole().getRoleId() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can edit food.");
        }

        Food updatedFood = foodService.updateFood(id, foodDetails);
        return ResponseEntity.ok(updatedFood);
    }

    // Delete food by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable int id, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();

        if (admin.getRole().getRoleId() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can delete food.");
        }

        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
}
