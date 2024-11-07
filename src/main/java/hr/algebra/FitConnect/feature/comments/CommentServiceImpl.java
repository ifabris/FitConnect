package hr.algebra.FitConnect.feature.comments;

import hr.algebra.FitConnect.feature.comments.DTO.CommentDTO;
import hr.algebra.FitConnect.feature.post.Post;
import hr.algebra.FitConnect.feature.post.PostRepo;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepo postRepository;
    private final UserRepo userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepo postRepository, UserRepo userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Comment addComment(Integer postId, Integer userId, String text) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setText(text);

        return commentRepository.save(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Fetch comments for the post and map to CommentDTO
        return commentRepository.findByPost(post).stream()
                .map(comment -> new CommentDTO(
                        comment.getCommentId(),
                        comment.getUser().getUsername(),
                        comment.getText()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }
}


