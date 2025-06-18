package com.example.eventticket.filter;

import java.io.IOException;
import java.security.Principal;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.eventticket.utils.JwtUtil;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching
public class JwtFilter implements ContainerRequestFilter {
    private static final String[] EXCLUDED_ENDPOINTS = {
            "/users/register",
            "/users/login",
    };

    private static final String[] EXCLUDED_PREFIXES = {
            "/events",
    };

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // TEMPORARILY DISABLED FOR TESTING
//        return;

        // Do not filter requests to excluded endpoints
        String path = requestContext.getUriInfo().getPath();

        // Check exact matches
        for (String endpoint : EXCLUDED_ENDPOINTS) {
            System.out.println("Checking exact path: " + path + " against " + endpoint);
            if (path.equals(endpoint)) {
                return;
            }
        }

        // Check prefix matches (for endpoints like /events, /events/1, /events/available)
        for (String prefix : EXCLUDED_PREFIXES) {
            System.out.println("Checking prefix path: " + path + " against " + prefix);
            if (path.startsWith(prefix)) {
                return;
            }
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abort(requestContext, "Missing or invalid Authorization header");
            return;
        }

        try {
            String token = authHeader.substring("Bearer ".length());
            DecodedJWT decodedJWT = JwtUtil.parseToken(token);

            String role = decodedJWT.getClaim("role").asString();
            long userId = decodedJWT.getClaim("userId").asLong();

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> String.valueOf(userId);
                }

                @Override
                public boolean isUserInRole(String r) {
                    System.out.println(r + " is in role " + role);
                    return r.equals(role);
                }

                @Override
                public boolean isSecure() {
                    return requestContext.getUriInfo().getRequestUri().getScheme().equals("https");
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });

        } catch (Exception e) {
            abort(requestContext, "Invalid or expired token");
        }
    }

    private void abort(ContainerRequestContext ctx, String msg) {
        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \"" + msg + "\"}")
                .build());
    }
}