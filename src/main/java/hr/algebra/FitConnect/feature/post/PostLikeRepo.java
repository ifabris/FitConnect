package hr.algebra.FitConnect.feature.post;

import hr.algebra.FitConnect.feature.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepo extends JpaRepository<PostLike, Integer> {
    boolean existsByUserAndPost(User user, Post post); // Use User and Post objects
    List<PostLike> findByPost(Post post);
}


