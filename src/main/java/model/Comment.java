package model;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private String authorId;
    private String authorUsername;
    private String message;
    private Instant timestamp;
}