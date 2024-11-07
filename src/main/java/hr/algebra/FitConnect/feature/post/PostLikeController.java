package hr.algebra.FitConnect.feature.post;

import hr.algebra.FitConnect.feature.post.request.PostLikeRequest;
import hr.algebra.FitConnect.feature.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post-likes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @GetMapping("/{postId}")
    public List<PostLike> getLikesForPost(@PathVariable int postId) {
        return postLikeService.getLikesForPost(postId);
    }

    @PostMapping
    public void likePost(@RequestBody PostLikeRequest postLikeRequest, Authentication authentication) {
        // Get the current user from the authentication context
        User currentUser = (User) authentication.getPrincipal();

        // Set the user ID in the PostLikeRequest
        postLikeRequest.setUserId(currentUser.getUserId()); // Assuming you have a setUserId method in PostLikeRequest

        // Pass the updated PostLikeRequest to the service method
        postLikeService.likePost(postLikeRequest);
    }


    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> unlikePost(@PathVariable int likeId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        // Call service to unlike the post, passing in the likeId and the current user's ID
        postLikeService.unlikePost(likeId, currentUser.getUserId());

        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

}
