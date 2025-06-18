package com.example.eventticket.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.eventticket.models.Event;
import com.example.eventticket.models.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class EventService {
    @PersistenceContext(unitName = "eventticket-pu")
    private EntityManager em;

    public Event create(Event event) {
        // Simple validation
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new RuntimeException("Event name is required");
        }
        if (event.getEventDate() == null || event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Event date must be in the future");
        }
        if (event.getMaxTickets() <= 0) {
            throw new RuntimeException("Max tickets must be positive");
        }
        if (event.getTicketPrice() == null || event.getTicketPrice().doubleValue() < 0) {
            throw new RuntimeException("Ticket price must be non-negative");
        }

        // Set available tickets to max tickets initially
        event.setAvailableTickets(event.getMaxTickets());

        em.persist(event);
        return event;
    }

    public List<Event> getAll() {
        return em.createQuery("SELECT e FROM Event e ORDER BY e.eventDate", Event.class).getResultList();
    }

    public Event findById(Long id) {
        Event event = em.find(Event.class, id);
        if (event == null) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        return event;
    }

    public List<Event> getAvailableEvents() {
        return em.createQuery(
            "SELECT e FROM Event e WHERE e.availableTickets > 0 AND e.eventDate > :now ORDER BY e.eventDate",
            Event.class)
            .setParameter("now", LocalDateTime.now())
            .getResultList();
    }

    public List<Event> getEventsByHost(Long hostId) {
        return em.createQuery(
            "SELECT e FROM Event e WHERE e.host.id = :hostId ORDER BY e.eventDate",
            Event.class)
            .setParameter("hostId", hostId)
            .getResultList();
    }
}
