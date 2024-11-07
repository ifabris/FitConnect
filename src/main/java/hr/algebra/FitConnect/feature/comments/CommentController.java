package hr.algebra.FitConnect.feature.comments;

import hr.algebra.FitConnect.feature.comments.DTO.CommentDTO;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepo userRepository;

    @Autowired
    public CommentController(CommentService commentService, UserRepo userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    // Add a new comment
    @PostMapping("/{postId}")
    public Comment addComment(@PathVariable Integer postId, @RequestBody String text, Authentication authentication) {
        // Get the logged-in user's ID from Authentication
        User user = (User) authentication.getPrincipal();
        if (user.getUsername() == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can't read user.");
        }
        return commentService.addComment(postId, user.getUserId(), text);
    }

    // Get comments for a specific post
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Integer postId) {
        List<CommentDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }


    // Delete a comment
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Fetch the comment to verify ownership
        Comment comment = commentService.getCommentById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        // Check if the authenticated user is the owner of the comment
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this comment");
        }

        commentService.deleteComment(commentId);
    }
}

