package controller;

import dto.CreateUserRequest;
import dto.TicketResponse;
import model.Ticket;
import model.TicketStatus;
import model.User;
import service.TicketService;
import service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final TicketService ticketService;

    // list users
    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // create user
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest req){
        User u = User.builder()
                .username(req.getUsername())
                .password(req.getPassword()) // will be encoded in service layer if needed
                .roles(req.getRoles())
                .build();
        return ResponseEntity.ok(userService.save(u));
    }

    // delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // view all tickets
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponse>> listAllTickets(){
        List<Ticket> tickets = ticketService.listAll();
        List<TicketResponse> resp = tickets.stream().map(t -> TicketResponse.builder()
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
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    // force reassign
    @PostMapping("/tickets/{id}/assign/{assigneeId}")
    public ResponseEntity<TicketResponse> forceAssign(@PathVariable String id, @PathVariable String assigneeId){
        Ticket updated = ticketService.assignTicket(id, assigneeId);
        return ResponseEntity.ok(TicketResponse.builder()
                .id(updated.getId())
                .subject(updated.getSubject())
                .description(updated.getDescription())
                .ownerId(updated.getOwnerId())
                .ownerUsername(updated.getOwnerUsername())
                .assigneeId(updated.getAssigneeId())
                .assigneeUsername(updated.getAssigneeUsername())
                .status(updated.getStatus())
                .priority(updated.getPriority())
                .createdAt(updated.getCreatedAt())
                .updatedAt(updated.getUpdatedAt())
                .comments(updated.getComments())
                .history(updated.getHistory())
                .build());
    }

    // force status change (resolve/close)
    @PostMapping("/tickets/{id}/status/{status}")
    public ResponseEntity<TicketResponse> changeStatus(@PathVariable String id, @PathVariable TicketStatus status){
        Ticket updated = ticketService.changeStatus(id, status, "ADMIN");
        return ResponseEntity.ok(TicketResponse.builder()
                .id(updated.getId())
                .subject(updated.getSubject())
                .description(updated.getDescription())
                .ownerId(updated.getOwnerId())
                .ownerUsername(updated.getOwnerUsername())
                .assigneeId(updated.getAssigneeId())
                .assigneeUsername(updated.getAssigneeUsername())
                .status(updated.getStatus())
                .priority(updated.getPriority())
                .createdAt(updated.getCreatedAt())
                .updatedAt(updated.getUpdatedAt())
                .comments(updated.getComments())
                .history(updated.getHistory())
                .build());
    }
}
