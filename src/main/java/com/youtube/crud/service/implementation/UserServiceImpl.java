package com.youtube.crud.service.implementation;

import com.youtube.crud.model.Ticket;
import com.youtube.crud.model.User;
import com.youtube.crud.repo.TicketRepo;
import com.youtube.crud.repo.UserRepo;
import com.youtube.crud.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TicketRepo ticketRepo;

    @Override
    public User create(User user) {
        return userRepo.save(user);
    }


    @Override
    public List<User> getAllUsers()
    {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User update(User newUser, Long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            user.setName(newUser.getName());
            user.setLastname(newUser.getLastname());
            user.setEmail(newUser.getEmail());
            user.setDob(newUser.getDob());
            user = userRepo.save(user);
            List<Ticket> tickets = ticketRepo.findByAssigned(user);
            for (Ticket ticket : tickets) {
                ticket.setAssigned(user);
                ticketRepo.save(ticket);
            }
        }
        return user;
    }
    private void updateTicketsForUser(User user) {
        List<Ticket> tickets = ticketRepo.findByAssigned(user);
        if (tickets != null) {
            for (Ticket ticket : tickets) {
                // Update assigned field in each ticket
                ticket.setAssigned(user);
                ticketRepo.save(ticket);
            }
        }
    }

    @Override
    public void delete(Long id) {
        userRepo.findById(id).ifPresent(user -> userRepo.delete(user));
    }

}
