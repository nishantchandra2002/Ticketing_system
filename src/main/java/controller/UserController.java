package controller;

import dto.*;
import model.*;
import service.TicketService;
import service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TicketService ticketService;

    // create ticket
    @PostMapping("/tickets")
    public ResponseEntity<TicketResponse> createTicket(@RequestBody CreateTicketRequest req, Authentication auth) {
        String username = auth.getName();
        User owner = userService.getByUsername(username);
        Ticket t = ticketService.createTicket(req.getSubject(), req.getDescription(), req.getPriority(), owner);
        return ResponseEntity.ok(toResponse(t));
    }

    // list own tickets
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponse>> listOwnTickets(Authentication auth) {
        String username = auth.getName();
        User owner = userService.getByUsername(username);
        List<Ticket> tickets = ticketService.listTicketsByOwner(owner.getId());
        List<TicketResponse> resp = tickets.stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    // add comment to own ticket (or any ticket owner created)
    @PostMapping("/tickets/{id}/comments")
    public ResponseEntity<TicketResponse> addComment(@PathVariable String id, @RequestBody CommentRequest req, Authentication auth) {
        String username = auth.getName();
        User u = userService.getByUsername(username);
        Ticket t = ticketService.getById(id);
        if (!t.getOwnerId().equals(u.getId()) && !u.getRoles().contains("AGENT") && !u.getRoles().contains("ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        Ticket updated = ticketService.addComment(id, u.getId(), u.getUsername(), req.getMessage());
        return ResponseEntity.ok(toResponse(updated));
    }

    // reassign ticket (user can reassign only if user is AGENT or ADMIN or owner and role permits)
    @PostMapping("/tickets/{id}/reassign")
    public ResponseEntity<TicketResponse> reassign(@PathVariable String id, @RequestBody ReassignRequest req, Authentication auth) {
        String username = auth.getName();
        User u = userService.getByUsername(username);
        Ticket t = ticketService.getById(id);
        boolean allowed = u.getRoles().contains("ADMIN") || u.getRoles().contains("AGENT") || t.getOwnerId().equals(u.getId());
        if (!allowed) return ResponseEntity.status(403).build();
        Ticket updated = ticketService.assignTicket(id, req.getAssigneeId());
        return ResponseEntity.ok(toResponse(updated));
    }

    // get single ticket (owner/assignee/admin/agent roles)
    @GetMapping("/tickets/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable String id, Authentication auth) {
        String username = auth.getName();
        User u = userService.getByUsername(username);
        Ticket t = ticketService.getById(id);
        boolean allowed = t.getOwnerId().equals(u.getId()) || (t.getAssigneeId()!=null && t.getAssigneeId().equals(u.getId()))
                || u.getRoles().contains("ADMIN") || u.getRoles().contains("AGENT");
        if (!allowed) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(toResponse(t));
    }

    private TicketResponse toResponse(Ticket t) {
        return TicketResponse.builder()
                .id(t.getId())
                .subject(t.getSubject())
                .description(t.getDescription())
                .ownerId(t.getOwnerId())
                .ownerUsername(t.getOwnerUsername())
                .assigneeId(t.getAssigneeId())
                .assigneeUsername(t.getAssigneeUsername())
                .status(t.getStatus())
                .priority(t.getPriority())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .comments(t.getComments())
                .history(t.getHistory())
                .build();
    }
}
