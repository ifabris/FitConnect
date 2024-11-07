package hr.algebra.FitConnect.feature.post.request;

import lombok.Data;

@Data
public class PostLikeRequest {
    private int postId; // The ID of the post being liked
    private int userId;
}
