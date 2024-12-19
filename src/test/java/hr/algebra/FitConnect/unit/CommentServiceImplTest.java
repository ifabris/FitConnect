package hr.algebra.FitConnect.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hr.algebra.FitConnect.feature.comments.Comment;
import hr.algebra.FitConnect.feature.comments.CommentRepository;
import hr.algebra.FitConnect.feature.comments.CommentServiceImpl;
import hr.algebra.FitConnect.feature.comments.DTO.CommentDTO;
import hr.algebra.FitConnect.feature.post.Post;
import hr.algebra.FitConnect.feature.post.PostRepo;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepo postRepository;

    @Mock
    private UserRepo userRepository;

    private User mockUser;
    private Post mockPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock User
        mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("test_user");

        // Mock Post
        mockPost = new Post();
        mockPost.setPostId(1);
        mockPost.setContent("Test Post");
    }

    @Test
    void testAddComment_Success() {
        String commentText = "Test Comment";

        when(postRepository.findById(mockPost.getPostId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = commentService.addComment(mockPost.getPostId(), mockUser.getUserId(), commentText);

        assertNotNull(result);
        assertEquals(commentText, result.getText());
        assertEquals(mockPost, result.getPost());
        assertEquals(mockUser, result.getUser());

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testAddComment_PostNotFound() {
        when(postRepository.findById(mockPost.getPostId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                commentService.addComment(mockPost.getPostId(), mockUser.getUserId(), "Test Comment")
        );

        assertEquals("404 NOT_FOUND \"Post not found\"", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testGetCommentsByPost_Success() {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setPost(mockPost);
        comment.setUser(mockUser);
        comment.setText("Test Comment");

        when(postRepository.findById(mockPost.getPostId())).thenReturn(Optional.of(mockPost));
        when(commentRepository.findByPost(mockPost)).thenReturn(List.of(comment));

        List<CommentDTO> result = commentService.getCommentsByPost(mockPost.getPostId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Comment", result.get(0).getCommentText());
        assertEquals("test_user", result.get(0).getUsername());

        verify(commentRepository, times(1)).findByPost(mockPost);
    }

    @Test
    void testGetCommentsByPost_PostNotFound() {
        when(postRepository.findById(mockPost.getPostId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                commentService.getCommentsByPost(mockPost.getPostId())
        );

        assertEquals("404 NOT_FOUND \"Post not found\"", exception.getMessage());
        verify(commentRepository, never()).findByPost(any(Post.class));
    }

    @Test
    void testDeleteComment_Success() {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setUser(mockUser);

        when(commentRepository.existsById(comment.getCommentId())).thenReturn(true);

        commentService.deleteComment(comment.getCommentId());

        verify(commentRepository, times(1)).deleteById(comment.getCommentId());
    }

    @Test
    void testDeleteComment_NotFound() {
        when(commentRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                commentService.deleteComment(1L)
        );

        assertEquals("404 NOT_FOUND \"Comment not found\"", exception.getMessage());
        verify(commentRepository, never()).deleteById(anyLong());
    }
}

