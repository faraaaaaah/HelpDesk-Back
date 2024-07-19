package com.youtube.crud.repo;

import com.youtube.crud.model.Ticket;
import com.youtube.crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Long> {
    List<Ticket> findByAssigned(User user);
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'TREATED'")
    int countTreatedTickets();

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'IN_PROGRESS'")
    int countInProgressTickets();

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'DRAFT'")
    int countDraftTickets();

    long countByStatus(String treated);
    @Query("SELECT t FROM Ticket t WHERE YEAR(t.date) = :year AND MONTH(t.date) = :month")
    List<Ticket> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
