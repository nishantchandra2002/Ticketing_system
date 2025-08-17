package repository;

import model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByOwnerId(String ownerId);
    List<Ticket> findByAssigneeId(String assigneeId);
}