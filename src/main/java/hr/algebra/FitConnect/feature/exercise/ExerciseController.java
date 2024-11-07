package hr.algebra.FitConnect.feature.exercise;

import hr.algebra.FitConnect.feature.exercise.DTO.ExerciseDTO;
import hr.algebra.FitConnect.feature.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseServiceImpl exerciseService;

    @PostMapping("/{workoutId}")
    public ResponseEntity<ExerciseDTO> createExercise(@PathVariable int workoutId, @RequestBody ExerciseDTO exerciseDTO, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();

        if (admin.getRole().getRoleId() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can add exercises.");
        }

        ExerciseDTO createdExercise = exerciseService.addExerciseToWorkout(workoutId, exerciseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExercise);
    }

    @PostMapping()
    public ResponseEntity<ExerciseDTO> createExercisewithoutWorkout(@RequestBody ExerciseDTO exerciseDTO, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();

        if (admin.getRole().getRoleId() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can add exercises.");
        }

        ExerciseDTO createdExercise = exerciseService.addExercise( exerciseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExercise);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        List<ExerciseDTO> exercises = exerciseService.getAllExercises();
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable int id) {
        ExerciseDTO exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(exercise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> updateExercise(@PathVariable int id, @RequestBody ExerciseDTO exerciseDTO, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();

        if (admin.getRole().getRoleId() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can edit exercises.");
        }
        ExerciseDTO updatedExercise = exerciseService.updateExercise(id, exerciseDTO);
        return ResponseEntity.ok(updatedExercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable int id, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();

        if (admin.getRole().getRoleId() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can delete exercises.");
        }
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

}

