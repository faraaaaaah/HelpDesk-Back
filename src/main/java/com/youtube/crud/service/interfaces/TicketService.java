package com.youtube.crud.service.interfaces;

import com.youtube.crud.model.Ticket;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface TicketService {
    Ticket create(Ticket ticket);
    List<Ticket> getAllTickets() ;
    Ticket getTicketById(Long id);
    Ticket update(Ticket ticket,Long id);
    void delete(Long id);
     Map<String, Double> calculateTicketPercentages();
    double calculateAverageForMonth(YearMonth yearMonth);
    Map<String, Double> calculateMonthlyAverages(int year);



    }
