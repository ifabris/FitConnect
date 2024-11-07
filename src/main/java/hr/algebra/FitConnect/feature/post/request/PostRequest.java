package hr.algebra.FitConnect.feature.post.request;

import lombok.Data;

@Data
public class PostRequest {
    private String content;  // The content of the post
    private String picture;  // URL of the post picture (optional)
}
