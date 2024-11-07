package hr.algebra.FitConnect.feature.post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(int id);
    Post savePost(Post post);
    void deletePost(int id);
}
