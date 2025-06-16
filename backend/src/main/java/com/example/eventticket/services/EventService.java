package com.example.eventticket.services;

import java.util.List;

import com.example.eventticket.models.Event;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class EventService {
    @PersistenceContext(unitName = "eventticket-pu")
    private EntityManager em;

    public Event create(Event event) {
        em.persist(event);
        return event;
    }

    public List<Event> getAll() {
        return em.createQuery("SELECT e FROM Event e", Event.class).getResultList();
    }
}
