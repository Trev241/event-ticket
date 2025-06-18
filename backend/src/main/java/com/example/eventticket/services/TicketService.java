package com.example.eventticket.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.eventticket.dto.EmailDTO;
import com.example.eventticket.models.Event;
import com.example.eventticket.models.Ticket;
import com.example.eventticket.models.User;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Stateless
public class TicketService {
    @PersistenceContext(unitName = "eventticket-pu")
    private EntityManager em;

    @Inject
    private EmailService emailService;

    @Transactional
    public Ticket book(Long userId, Long eventId, int quantity) {
        // Validation
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }
        if (quantity > 5) {
            throw new RuntimeException("Maximum 5 tickets per booking");
        }

        Event event = em.find(Event.class, eventId);
        User user = em.find(User.class, userId);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Check if event is in the future
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot book tickets for past events");
        }

        // Check availability
        if (event.getAvailableTickets() < quantity) {
            throw new RuntimeException("Only " + event.getAvailableTickets() + " tickets available");
        }

        // Check if user already has a ticket for this event (simple business rule)
        List<Ticket> existingTickets = em.createQuery(
            "SELECT t FROM Ticket t WHERE t.user.id = :userId AND t.event.id = :eventId",
            Ticket.class)
            .setParameter("userId", userId)
            .setParameter("eventId", eventId)
            .getResultList();

        if (!existingTickets.isEmpty()) {
            throw new RuntimeException("You already have a ticket for this event");
        }

        // Update available tickets
        event.setAvailableTickets(event.getAvailableTickets() - quantity);
        em.merge(event);

        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setBookingTime(LocalDateTime.now());
        ticket.setQuantity(quantity);
        ticket.setStatus("CONFIRMED");

        EmailDTO email = new EmailDTO(
                user.getEmail(),
                "Ticket Booked",
                "Hey " + user.getUsername() + ", your ticket has been booked! See you soon ;)"
        );
        emailService.sendEmail(email);

        em.persist(ticket);
        return ticket;
    }

    // Overloaded method for backward compatibility
    public Ticket book(Long userId, Long eventId) {
        return book(userId, eventId, 1);
    }

    public List<Ticket> getAll() {
        return em.createQuery("SELECT t FROM Ticket t ORDER BY t.bookingTime DESC", Ticket.class).getResultList();
    }

    public List<Ticket> getTicketsByUser(Long userId) {
        return em.createQuery(
            "SELECT t FROM Ticket t WHERE t.user.id = :userId ORDER BY t.bookingTime DESC",
            Ticket.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    public List<Ticket> getTicketsByEvent(Long eventId) {
        return em.createQuery(
            "SELECT t FROM Ticket t WHERE t.event.id = :eventId ORDER BY t.bookingTime DESC",
            Ticket.class)
            .setParameter("eventId", eventId)
            .getResultList();
    }

    @Transactional
    public void cancelTicket(Long ticketId, Long userId) {
        Ticket ticket = em.find(Ticket.class, ticketId);

        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }

        if (ticket.getUser().getId() != userId) {
            throw new RuntimeException("You can only cancel your own tickets");
        }

        if (!"CONFIRMED".equals(ticket.getStatus())) {
            throw new RuntimeException("Ticket is already cancelled");
        }

        // Check if event is in the future (allow cancellation only before event)
        if (ticket.getEvent().getEventDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel tickets for past events");
        }

        // Return tickets to available pool
        Event event = ticket.getEvent();
        event.setAvailableTickets(event.getAvailableTickets() + ticket.getQuantity());
        em.merge(event);

        // Mark ticket as cancelled
        ticket.setStatus("CANCELLED");
        em.merge(ticket);
    }
}
