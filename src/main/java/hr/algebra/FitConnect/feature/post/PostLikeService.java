package hr.algebra.FitConnect.feature.post;

import hr.algebra.FitConnect.feature.post.request.PostLikeRequest;

import java.util.List;

public interface PostLikeService {
    List<PostLike> getLikesForPost(int postId);
    void likePost(PostLikeRequest request);
    void unlikePost(int likeId, int userId);
}
