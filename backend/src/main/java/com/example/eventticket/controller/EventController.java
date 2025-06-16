package com.example.eventticket.controller;

import com.example.eventticket.models.Event;
import com.example.eventticket.services.EventService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventController {
    @Inject private EventService eventService;

    @POST
    public Event createEvent(Event event) {
        return eventService.create(event);
    }

    @GET
    public List<Event> getEvents() {
        return eventService.getAll();
    }
}
