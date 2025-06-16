package com.example.eventticket.controller;

import com.example.eventticket.models.Ticket;
import com.example.eventticket.services.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

@Path("/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketController {
    @Inject private TicketService ticketService;

    @POST
    @Path("/book")
    public Ticket book(@QueryParam("userId") Long userId, @QueryParam("eventId") Long eventId) {
        return ticketService.book(userId, eventId);
    }

    @GET
    public List<Ticket> getAll() {
        return ticketService.getAll();
    }
}
