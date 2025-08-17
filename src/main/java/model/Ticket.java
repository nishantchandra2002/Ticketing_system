package model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id;
    private String subject;
    private String description;
    private String ownerId;    // Which user created the ticket
    private String ownerUsername;
    private String assigneeId; // Which agent is Assigned
    private String assigneeUsername;
    private TicketStatus status;
    private String priority; //  LOW, MEDIUM, HIGH
    private Instant createdAt;
    private Instant updatedAt;
    private List<Comment> comments = new ArrayList<>();
    private List<String> history = new ArrayList<>(); // textual history notes
}