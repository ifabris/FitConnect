package hr.algebra.FitConnect.feature.messages;

import hr.algebra.FitConnect.feature.messages.request.MessageRequest;
import hr.algebra.FitConnect.feature.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getChatUsers(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<User> chatUsers = chatService.getChatUsers(currentUser);
        return ResponseEntity.ok(chatUsers);
    }
    @GetMapping("/history/{userId}")
    public List<Message> getChatHistory(Authentication authentication, @PathVariable Integer userId) {
        // Get the logged-in user's ID
        User loggedInUser = (User) authentication.getPrincipal();
        Integer loggedInUserId = loggedInUser.getUserId();

        // Call the service with both IDs
        return chatService.getChatHistory(loggedInUserId, userId);
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(Authentication authentication,@RequestBody MessageRequest messageRequest) {
        User sender = (User) authentication.getPrincipal();
        Message savedMessage = chatService.saveMessage(messageRequest, sender);
        return ResponseEntity.ok(savedMessage);
    }
}

