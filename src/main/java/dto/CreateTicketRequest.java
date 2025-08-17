package dto;

import lombok.Data;

@Data
public class CreateTicketRequest {
    private String subject;
    private String description;
    private String priority;
}