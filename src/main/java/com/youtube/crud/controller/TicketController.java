package com.youtube.crud.controller;


import com.youtube.crud.model.Ticket;
import com.youtube.crud.service.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("")
    public Ticket addTicket(@RequestBody Ticket ticket) {
        return ticketService.create(ticket);
    }
    @GetMapping("")
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }
    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable  Long id) {
        return ticketService.getTicketById(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@RequestBody Ticket ticket, @PathVariable Long id) {
        Ticket updatedTicket = ticketService.update(ticket, id);
        return ResponseEntity.ok(updatedTicket);
    }
    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.delete(id);
    }
    @GetMapping("/percentages")
    public ResponseEntity<Map<String, Double>> getTicketPercentages() {
        Map<String, Double> percentages = ticketService.calculateTicketPercentages();
        return ResponseEntity.ok().body(percentages);
    }
    @GetMapping("/average/{year}")
    public ResponseEntity<Map<String, Double>> getMonthlyAverages(@PathVariable int year) {
        Map<String, Double> averages = ticketService.calculateMonthlyAverages(year);
        return ResponseEntity.ok(averages);
    }

}
