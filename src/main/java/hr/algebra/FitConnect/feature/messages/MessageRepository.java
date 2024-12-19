package hr.algebra.FitConnect.feature.messages;

import hr.algebra.FitConnect.feature.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findBySender_UserIdAndReceiver_UserId(Integer senderId, Integer receiverId);

    List<Message> findByReceiver_UserIdAndSender_UserId(Integer receiverId, Integer senderId);

    @Query("SELECT m FROM Message m WHERE m.sender = :user OR m.receiver = :user")
    List<Message> findBySenderOrReceiver(@Param("user") User user);

}


