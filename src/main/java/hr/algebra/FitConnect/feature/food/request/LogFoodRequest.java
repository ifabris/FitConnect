package hr.algebra.FitConnect.feature.food.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LogFoodRequest {
    @NotNull
    private Integer foodId;

    @NotNull
    private Integer quantity;

    @NotNull
    private LocalDate logDate;
}
