package com.example.eventticket.dto;

public class BookingRequest {
    private Long eventId;
    private int quantity;

    public BookingRequest() {
    }

    public BookingRequest(Long eventId, int quantity) {
        this.eventId = eventId;
        this.quantity = quantity;
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