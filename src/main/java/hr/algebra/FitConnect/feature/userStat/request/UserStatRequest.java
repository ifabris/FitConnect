package hr.algebra.FitConnect.feature.userStat.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserStatRequest {
    private BigDecimal weight;
    private BigDecimal height;
}
