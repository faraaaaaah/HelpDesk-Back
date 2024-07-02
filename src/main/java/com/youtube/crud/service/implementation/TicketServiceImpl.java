package com.youtube.crud.service.implementation;

import com.youtube.crud.model.Ticket;
import com.youtube.crud.model.User;
import com.youtube.crud.repo.TicketRepo;
import com.youtube.crud.repo.UserRepo;
import com.youtube.crud.service.interfaces.TicketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private UserRepo userRepo;
    @Override
    public Ticket create(Ticket ticket) {
        return ticketRepo.save(ticket);
    }



    @Override
    public List<Ticket> getAllTickets()
    {
        return ticketRepo.findAll();
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepo.findById(id).orElse(null);
    }

    @Override
    public Ticket update(Ticket updatedTicket, Long id) {
        // Find the ticket by id or throw an exception if not found
        Ticket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found for id: " + id));
        if (updatedTicket.getAssigned() == null) {
            throw new IllegalArgumentException("Assigned user cannot be null");
        }
        // Find the assigned user by id or throw an exception if not found
        User assigned = userRepo.findById(updatedTicket.getAssigned().getId())
                .orElseThrow(() -> new EntityNotFoundException("Assigned user not found for id: " + updatedTicket.getAssigned().getId()));

        // Update the ticket entity with new values
        ticket.setAssigned(assigned);
        ticket.setName(updatedTicket.getName());
        ticket.setDate(updatedTicket.getDate());
        ticket.setStatus(updatedTicket.getStatus());

        // Save the updated ticket entity
        return ticketRepo.save(ticket);
    }



    @Override
    public void delete(Long id) {
        ticketRepo.findById(id).ifPresent(ticket -> ticketRepo.delete(ticket));
    }

}
