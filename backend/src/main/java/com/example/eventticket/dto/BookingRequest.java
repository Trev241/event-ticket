package com.example.eventticket.dto;

public class BookingRequest {
    private Long userId;
    private Long eventId;
    private int quantity;

    public BookingRequest() {
    }

    public BookingRequest(Long userId, Long eventId, int quantity) {
        this.userId = userId;
        this.eventId = eventId;
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
} 