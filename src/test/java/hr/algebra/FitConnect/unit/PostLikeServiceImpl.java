package hr.algebra.FitConnect.unit;

import hr.algebra.FitConnect.feature.post.*;
import hr.algebra.FitConnect.feature.post.request.PostLikeRequest;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceImplTest {

    @InjectMocks
    private PostLikeServiceImpl postLikeService;

    @Mock
    private PostLikeRepo postLikeRepo;

    @Mock
    private PostRepo postRepo;

    @Mock
    private UserRepo userRepo;

    private Post post;
    private User user;
    private PostLike postLike;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("testUser");

        post = new Post();
        post.setPostId(1);
        post.setContent("Test Post");
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        postLike = new PostLike();
        postLike.setLikeId(1);
        postLike.setPost(post);
        postLike.setUser(user);
        postLike.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getLikesForPost_Success() {
        when(postRepo.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(postLikeRepo.findByPost(post)).thenReturn(List.of(postLike));

        List<PostLike> likes = postLikeService.getLikesForPost(post.getPostId());

        assertEquals(1, likes.size());
        assertEquals(postLike, likes.get(0));
        verify(postRepo, times(1)).findById(post.getPostId());
        verify(postLikeRepo, times(1)).findByPost(post);
    }

    @Test
    void getLikesForPost_PostNotFound() {
        when(postRepo.findById(post.getPostId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> postLikeService.getLikesForPost(post.getPostId()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());
    }

    @Test
    void likePost_Success() {
        PostLikeRequest request = new PostLikeRequest();
        request.setPostId(post.getPostId());
        request.setUserId(user.getUserId());

        when(postRepo.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(userRepo.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(postLikeRepo.existsByUserAndPost(user, post)).thenReturn(false);

        postLikeService.likePost(request);

        verify(postLikeRepo, times(1)).save(any(PostLike.class));
    }

    @Test
    void likePost_AlreadyLiked() {
        PostLikeRequest request = new PostLikeRequest();
        request.setPostId(post.getPostId());
        request.setUserId(user.getUserId());

        when(postRepo.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(userRepo.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(postLikeRepo.existsByUserAndPost(user, post)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> postLikeService.likePost(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User has already liked this post", exception.getReason());
    }

    @Test
    void unlikePost_Success() {
        when(postLikeRepo.findById(postLike.getLikeId())).thenReturn(Optional.of(postLike));

        postLikeService.unlikePost(postLike.getLikeId(), user.getUserId());

        verify(postLikeRepo, times(1)).deleteById(postLike.getLikeId());
    }

    @Test
    void unlikePost_NotOwner() {
        User otherUser = new User();
        otherUser.setUserId(2);
        postLike.setUser(otherUser);

        when(postLikeRepo.findById(postLike.getLikeId())).thenReturn(Optional.of(postLike));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> postLikeService.unlikePost(postLike.getLikeId(), user.getUserId()));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("You can only unlike your own likes.", exception.getReason());
    }

    @Test
    void unlikePost_LikeNotFound() {
        when(postLikeRepo.findById(postLike.getLikeId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> postLikeService.unlikePost(postLike.getLikeId(), user.getUserId()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Like not found", exception.getReason());
    }
}

