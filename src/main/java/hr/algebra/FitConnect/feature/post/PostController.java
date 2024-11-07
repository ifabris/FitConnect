package hr.algebra.FitConnect.feature.post;

import hr.algebra.FitConnect.feature.post.request.PostRequest;
import hr.algebra.FitConnect.feature.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest, Authentication authentication) {
        // Get the currently authenticated user's ID (if you want to ensure the user is creating their own posts)
        User currentUser = (User) authentication.getPrincipal();

        // Create a new post and associate the user
        Post newPost = new Post();
        newPost.setUser(currentUser); // Associate the current user with the post
        newPost.setContent(postRequest.getContent());
        newPost.setPicture(postRequest.getPicture());
        newPost.setCreatedAt(LocalDateTime.now()); // Set createdAt if you have it in your Post entity

        Post savedPost = postService.savePost(newPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable int postId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        // Fetch the post to ensure it exists
        Post post = postService.getPostById(postId);

        // Check if the current user is the creator of the post or an admin
        if (!post.getUser().getUserId().equals(currentUser.getUserId()) && currentUser.getRole().getRoleId() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own posts or be an admin to delete.");
        }

        // Proceed to delete the post
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

}
