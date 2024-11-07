package hr.algebra.FitConnect.feature.user;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserCoachId implements Serializable {
    private int userId;
    private int coachId;

    // Default constructor
    public UserCoachId() {}

    public UserCoachId(int userId, int coachId) {
        this.userId = userId;
        this.coachId = coachId;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    // Override equals() and hashCode() for proper comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCoachId)) return false;

        UserCoachId that = (UserCoachId) o;

        if (userId != that.userId) return false;
        return coachId == that.coachId;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + coachId;
        return result;
    }
}
