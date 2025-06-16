package com.example.eventticket.services;

import com.example.eventticket.models.Event;
import com.example.eventticket.models.Ticket;
import com.example.eventticket.models.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class TicketService {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public Ticket book(Long userId, Long eventId) {
        Event event = em.find(Event.class, eventId);
        User user = em.find(User.class, userId);

        if (event == null || user == null) {
            throw new RuntimeException("Invalid user or event");
        }

        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No available seats");
        }

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        em.merge(event);

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setBookingTime(LocalDateTime.now());

        em.persist(ticket);
        return ticket;
    }

    public List<Ticket> getAll() {
        return em.createQuery("SELECT t FROM Ticket t", Ticket.class).getResultList();
    }
}
