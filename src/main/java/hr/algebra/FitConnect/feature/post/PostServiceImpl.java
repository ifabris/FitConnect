package hr.algebra.FitConnect.feature.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepository;

    @Autowired
    private PostLikeRepo postLikeRepo;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post savePost(Post post) {
        post.setCreatedAt(LocalDateTime.now()); // Set created time
        return postRepository.save(post);
    }

    @Override
    public void deletePost(int postId) {
        // Check if the post exists
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        // Optionally delete associated likes
        List<PostLike> likes = postLikeRepo.findByPost(postRepository.findById(postId).get());
        if (!likes.isEmpty()) {
            postLikeRepo.deleteAll(likes);
        }

        // Delete the post
        postRepository.deleteById(postId);
    }
}
