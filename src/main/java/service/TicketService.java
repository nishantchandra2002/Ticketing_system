package service;

import model.*;
import repository.TicketRepository;
import repository.UserRepository;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public Ticket createTicket(String subject, String description, String priority, User owner) {
        Ticket t = Ticket.builder()
                .subject(subject)
                .description(description)
                .priority(priority)
                .ownerId(owner.getId())
                .ownerUsername(owner.getUsername())
                .status(TicketStatus.OPEN)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        t.getHistory().add("Ticket created by " + owner.getUsername() + " at " + Instant.now());
        return ticketRepository.save(t);
    }

    public List<Ticket> listTicketsByOwner(String ownerId) {
        return ticketRepository.findByOwnerId(ownerId);
    }

    public Ticket getById(String id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + id));
    }

    public Ticket addComment(String ticketId, String authorId, String authorUsername, String message) {
        Ticket t = getById(ticketId);
        Comment c = Comment.builder()
                .authorId(authorId)
                .authorUsername(authorUsername)
                .message(message)
                .timestamp(Instant.now())
                .build();
        t.getComments().add(c);
        t.getHistory().add("Comment added by " + authorUsername + " at " + Instant.now());
        t.setUpdatedAt(Instant.now());
        return ticketRepository.save(t);
    }

    public Ticket assignTicket(String ticketId, String assigneeId) {
        Ticket t = getById(ticketId);
        User agent = userRepository.findById(assigneeId).orElseThrow(() -> new ResourceNotFoundException("User not found: " + assigneeId));
        t.setAssigneeId(agent.getId());
        t.setAssigneeUsername(agent.getUsername());
        t.getHistory().add("Assigned to " + agent.getUsername() + " at " + Instant.now());
        t.setUpdatedAt(Instant.now());
        if (t.getStatus() == TicketStatus.OPEN) t.setStatus(TicketStatus.IN_PROGRESS);
        return ticketRepository.save(t);
    }

    public Ticket changeStatus(String ticketId, TicketStatus status, String byWho) {
        Ticket t = getById(ticketId);
        t.setStatus(status);
        t.getHistory().add("Status changed to " + status + " by " + byWho + " at " + Instant.now());
        t.setUpdatedAt(Instant.now());
        return ticketRepository.save(t);
    }

    public List<Ticket> listAll() {
        return ticketRepository.findAll();
    }
}
