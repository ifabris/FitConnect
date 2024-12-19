package hr.algebra.FitConnect.feature.messages.request;

import lombok.Data;

@Data
public class MessageRequest {
    private Integer receiverId;
    private String content;
}

