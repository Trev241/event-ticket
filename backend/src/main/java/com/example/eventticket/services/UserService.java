package com.example.eventticket.services;

import com.example.eventticket.exceptions.UsernameAlreadyTakenException;
import com.example.eventticket.models.User;
import com.example.eventticket.utils.JwtUtil;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public User register(User user) {
        List<User> users = em.createQuery(
                "SELECT u FROM User u WHERE u.username = '" + user.getUsername() + "'",
                User.class).getResultList();

        if (!users.isEmpty()) {
            throw new UsernameAlreadyTakenException("User with username " + user.getUsername() + " already exists");
        } else {
            em.persist(user);
            return user;
        }
    }

    public String login(String username, String password) {
        List<User> users = em.createQuery(
                "SELECT u FROM User u " +
                        "WHERE u.username = '" + username + "' " +
                        "AND u.password = '" + password + "'",
                User.class).getResultList();

        if (!users.isEmpty()) {
            User user = users.getFirst();
            return JwtUtil.generateToken(user.getUsername(), user.getRole());
        }

        return null;
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
