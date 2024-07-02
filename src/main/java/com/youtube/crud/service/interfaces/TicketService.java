package com.youtube.crud.service.interfaces;

import com.youtube.crud.model.Ticket;

import java.util.List;

public interface TicketService {
    Ticket create(Ticket ticket);
    List<Ticket> getAllTickets() ;
    Ticket getTicketById(Long id);
    Ticket update(Ticket ticket,Long id);
    void delete(Long id);

}
