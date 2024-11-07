package hr.algebra.FitConnect.feature.user.response;

import lombok.Data;

@Data
public class UserDetails {
    private Integer id;
    private String username;
    private String email;
    private String picture;
}
