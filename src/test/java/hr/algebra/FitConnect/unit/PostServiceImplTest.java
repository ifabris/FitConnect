package hr.algebra.FitConnect.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hr.algebra.FitConnect.feature.post.Post;
import hr.algebra.FitConnect.feature.post.PostRepo;
import hr.algebra.FitConnect.feature.post.PostServiceImpl;
import hr.algebra.FitConnect.feature.post.PostLike;
import hr.algebra.FitConnect.feature.post.PostLikeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class PostServiceImplTest {

    @Mock
    private PostRepo postRepo;

    @Mock
    private PostLikeRepo postLikeRepo;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPosts_ShouldReturnAllPosts() {
        Post post1 = new Post();
        post1.setPostId(1);
        post1.setContent("First post");

        Post post2 = new Post();
        post2.setPostId(2);
        post2.setContent("Second post");

        when(postRepo.findAll()).thenReturn(Arrays.asList(post1, post2));

        List<Post> posts = postService.getAllPosts();

        assertEquals(2, posts.size());
        assertEquals("First post", posts.get(0).getContent());
        assertEquals("Second post", posts.get(1).getContent());
    }

    @Test
    void getPostById_ShouldReturnPost_WhenPostExists() {
        Post post = new Post();
        post.setPostId(1);
        post.setContent("Sample post");

        when(postRepo.findById(1)).thenReturn(Optional.of(post));

        Post result = postService.getPostById(1);

        assertNotNull(result);
        assertEquals("Sample post", result.getContent());
    }

    @Test
    void getPostById_ShouldReturnNull_WhenPostDoesNotExist() {
        when(postRepo.findById(1)).thenReturn(Optional.empty());

        Post result = postService.getPostById(1);

        assertNull(result);
    }

    @Test
    void savePost_ShouldSaveAndReturnPost() {
        Post post = new Post();
        post.setContent("New post");

        when(postRepo.save(post)).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            savedPost.setPostId(1);
            savedPost.setCreatedAt(LocalDateTime.now());
            return savedPost;
        });

        Post savedPost = postService.savePost(post);

        assertNotNull(savedPost);
        assertEquals(1, savedPost.getPostId());
        assertEquals("New post", savedPost.getContent());
        assertNotNull(savedPost.getCreatedAt());
    }

    @Test
    void deletePost_ShouldDeletePost_WhenPostExists() {
        Post post = new Post();
        post.setPostId(1);
        when(postRepo.existsById(1)).thenReturn(true);
        when(postRepo.findById(1)).thenReturn(Optional.of(post));

        PostLike like1 = new PostLike();
        PostLike like2 = new PostLike();
        when(postLikeRepo.findByPost(post)).thenReturn(Arrays.asList(like1, like2));

        postService.deletePost(1);

        verify(postLikeRepo, times(1)).deleteAll(Arrays.asList(like1, like2));
        verify(postRepo, times(1)).deleteById(1);
    }

    @Test
    void deletePost_ShouldThrowException_WhenPostDoesNotExist() {
        when(postRepo.existsById(1)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> postService.deletePost(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());
    }
}
