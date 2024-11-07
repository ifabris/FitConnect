package hr.algebra.FitConnect.feature.comments;

import hr.algebra.FitConnect.feature.comments.DTO.CommentDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment addComment(Integer postId, Integer userId, String text);
    List<CommentDTO> getCommentsByPost(Integer postId);
    void deleteComment(Long commentId);
    Optional<Comment> getCommentById(Long commentId);
}
