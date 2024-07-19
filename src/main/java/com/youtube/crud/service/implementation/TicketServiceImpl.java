package com.youtube.crud.service.implementation;

import com.youtube.crud.model.Ticket;
import com.youtube.crud.model.User;
import com.youtube.crud.repo.TicketRepo;
import com.youtube.crud.repo.UserRepo;
import com.youtube.crud.service.interfaces.TicketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.time.ZoneId;


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
    @Override
    public Map<String, Double> calculateTicketPercentages() {
        List<Ticket> tickets = ticketRepo.findAll();

        double totalTickets = tickets.size();
        double treatedCount = 0;
        double inProgressCount = 0;
        double draftCount = 0;
        for (Ticket ticket : tickets) {
            switch (ticket.getStatus()) {
                case TREATED:
                    treatedCount++;
                    break;
                case IN_PROGRESS:
                    inProgressCount++;
                    break;
                case DRAFT:
                    draftCount++;
                    break;
                // Add more cases as needed
            }
        }
        Map<String, Double> percentages = new HashMap<>();
        percentages.put("treated", (treatedCount / totalTickets) * 100);
        percentages.put("inProgress", (inProgressCount / totalTickets) * 100);
        percentages.put("draft", (draftCount / totalTickets) * 100);

        return percentages;
    }

    public double calculateMonthlyAverageForMonth(int year, int month) {
        List<Ticket> allTickets = getAllTickets();
        if (allTickets.isEmpty()) {
            return 0.0;
        }

        long ticketsThisMonth = allTickets.stream()
                .filter(ticket -> {
                    LocalDate ticketDate = ticket.getDate();
                    return ticketDate.getYear() == year && ticketDate.getMonthValue() == month;
                })
                .count();

        // Calculate average only if there are tickets for the given month
        if (ticketsThisMonth == 0) {
            return 0.0;
        }

        // Calculate the average
        YearMonth yearMonth = YearMonth.of(year, month);
        return ticketsThisMonth / (double) yearMonth.lengthOfMonth();
    }
    public Map<String, Double> calculateMonthlyAverages(int year) {
        Map<String, Double> averages = new LinkedHashMap<>();

        // Loop through each month of the year
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            double average = calculateAverageForMonth(yearMonth);
            averages.put(yearMonth.getMonth().toString(), average);
        }

        return averages;
    }
    public double calculateAverageForMonth(YearMonth yearMonth) {
        List<Ticket> tickets = ticketRepo.findByYearAndMonth(yearMonth.getYear(), yearMonth.getMonthValue());
        if (tickets.isEmpty()) {
            return 0.0;
        }

        // Calculate average based on ticket count
        return (double) tickets.size() / yearMonth.lengthOfMonth();
    }
}
