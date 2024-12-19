package hr.algebra.FitConnect.feature.messages;

import hr.algebra.FitConnect.feature.messages.request.MessageRequest;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepo userRepo;

    public ChatService(MessageRepository messageRepository, UserRepo userRepo) {
        this.messageRepository = messageRepository;
        this.userRepo = userRepo;
    }

    public List<Message> getChatHistory(Integer userId1, Integer userId2) {
        List<Message> sentMessages = messageRepository.findBySender_UserIdAndReceiver_UserId(userId1, userId2);
        List<Message> receivedMessages = messageRepository.findByReceiver_UserIdAndSender_UserId(userId1, userId2);
        sentMessages.addAll(receivedMessages);
        return sentMessages;
    }

    public Message saveMessage(MessageRequest messageRequest, User sender) {

        // Get the receiver by ID
        User receiver = userRepo.findById(messageRequest.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Create and save the message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageRequest.getContent());
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<User> getChatUsers(User currentUser) {
        List<Message> messages = messageRepository.findBySenderOrReceiver(currentUser);
        return messages.stream()
                .flatMap(message -> Stream.of(message.getSender(), message.getReceiver()))
                .filter(user -> !user.equals(currentUser)) // Exclude the current user
                .distinct()
                .collect(Collectors.toList());
    }
}

