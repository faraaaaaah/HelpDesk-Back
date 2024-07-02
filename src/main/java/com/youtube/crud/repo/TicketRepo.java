package com.youtube.crud.repo;

import com.youtube.crud.model.Ticket;
import com.youtube.crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Long> {
    List<Ticket> findByAssigned(User user);

}
