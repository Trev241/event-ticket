package com.example.eventticket.controller;

import com.example.eventticket.dto.UserLoginDto;
import com.example.eventticket.exceptions.UsernameAlreadyTakenException;
import com.example.eventticket.models.User;
import com.example.eventticket.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public Response createUser(User user) {
        try {
            userService.register(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (UsernameAlreadyTakenException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    public Response login(UserLoginDto userLoginDto) {
        String token = userService.login(userLoginDto.getUsername(), userLoginDto.getPassword());
        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(token).build();
    }
}
