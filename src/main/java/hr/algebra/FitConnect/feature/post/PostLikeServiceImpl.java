package hr.algebra.FitConnect.feature.post;

import hr.algebra.FitConnect.feature.post.request.PostLikeRequest;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private PostLikeRepo postLikeRepository;

    @Autowired PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<PostLike> getLikesForPost(int postId) {
        // Fetch the post based on the postId
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Return likes for the specific post
        return postLikeRepository.findByPost(post); // Ensure this matches your repository method
    }

    @Override
    public void likePost(PostLikeRequest request) {
        // Fetch the post to ensure it exists
        Post post = postRepo.findById(request.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Get the current user from the authentication context
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Check if the user has already liked the post using User and Post objects
        boolean alreadyLiked = postLikeRepository.existsByUserAndPost(user, post);
        if (alreadyLiked) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already liked this post");
        }

        // If not already liked, create a new PostLike
        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(user);
        postLike.setCreatedAt(LocalDateTime.now());

        postLikeRepository.save(postLike);
    }




    @Override
    public void unlikePost(int likeId, int userId) {
        // Fetch the like to ensure it exists
        PostLike postLike = postLikeRepository.findById(likeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found"));

        // Check if the like belongs to the current user
        if (!postLike.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only unlike your own likes.");
        }

        // Proceed to delete the like
        postLikeRepository.deleteById(likeId);
    }

}
