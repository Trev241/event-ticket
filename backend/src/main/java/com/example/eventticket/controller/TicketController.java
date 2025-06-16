package com.example.eventticket.controller;

import java.util.List;

import com.example.eventticket.dto.BookingRequest;
import com.example.eventticket.models.Ticket;
import com.example.eventticket.services.TicketService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketController {
    @Inject private TicketService ticketService;

    @POST
    @Path("/book")
    public Response book(BookingRequest request) {
        try {
            Ticket ticket = ticketService.book(request.getUserId(), request.getEventId());
            return Response.status(Response.Status.CREATED).entity(ticket).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    public Response getAll() {
        try {
            List<Ticket> tickets = ticketService.getAll();
            return Response.ok(tickets).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
