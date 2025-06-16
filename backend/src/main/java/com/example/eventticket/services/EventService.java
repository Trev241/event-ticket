package com.example.eventticket.services;

import com.example.eventticket.models.Event;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class EventService {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public Event create(Event event) {
        em.persist(event);
        return event;
    }

    public List<Event> getAll() {
        return em.createQuery("SELECT e FROM Event e", Event.class).getResultList();
    }
}
