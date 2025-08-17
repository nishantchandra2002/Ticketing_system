package dto;

import model.Comment;
import model.TicketStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class TicketResponse {
    private String id;
    private String subject;
    private String description;
    private String ownerId;
    private String ownerUsername;
    private String assigneeId;
    private String assigneeUsername;
    private TicketStatus status;
    private String priority;
    private Instant createdAt;
    private Instant updatedAt;
    private List<Comment> comments;
    private List<String> history;
}