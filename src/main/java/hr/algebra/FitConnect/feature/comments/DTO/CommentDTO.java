package hr.algebra.FitConnect.feature.comments.DTO;

import lombok.Data;

@Data
public class CommentDTO {
    private Long commentId;
    private String username;
    private String commentText;

    // Constructor
    public CommentDTO(Long commentId, String username, String commentText) {
        this.commentId = commentId;
        this.username = username;
        this.commentText = commentText;
    }
}
