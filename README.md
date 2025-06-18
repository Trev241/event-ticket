# 🎫 Event Ticket Management System

A comprehensive event ticketing application built with **Jakarta EE 10**, **WildFly**, and **H2 Database** for educational purposes.

## 🚀 Features

- **User Management**: Registration, login with role-based access (Admin, User)
- **Event Management**: Create, view, and manage events with availability tracking
- **Ticket Booking**: Book tickets with quantity limits and duplicate prevention
- **Ticket Cancellation**: Cancel bookings with automatic availability restoration
- **Security**: Password hashing, input validation, SQL injection prevention
- **RESTful APIs**: Complete CRUD operations with proper error handling

## 🛠️ Technology Stack

- **Backend**: Jakarta EE 10, JAX-RS, JPA/Hibernate
- **Application Server**: WildFly 28
- **Database**: H2 (in-memory for development)
- **Build Tool**: Maven
- **Containerization**: Docker
- **Java Version**: 17

## 🏃 Quick Start

### Option 1: Docker (Recommended)

```bash
# Navigate to backend directory
cd backend

# Build and run with Docker
docker build -t event-ticket-backend .
docker run -d --name event-ticket-app -p 8081:8080 event-ticket-backend

# Access the application
# API Base: http://localhost:8081/event-ticket-1.0-SNAPSHOT/api
```

### Option 2: Local Development

```bash
cd backend
mvn clean package
# Deploy the WAR file to your WildFly server
```

## 📋 API Endpoints

### User Management

Endpoints marked with \* require authentication.

- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login
- `GET /api/users`\* - Get all users (admin only)

### Event Management

- `POST /api/events` - Create event
- `GET /api/events` - Get all events
- `GET /api/events/{id}` - Get specific event
- `GET /api/events/available` - Get events with available tickets

### Ticket Management

- `POST /api/tickets/book`\* - Book tickets
- `GET /api/tickets`\* - Get all tickets (admin only)
- `GET /api/tickets/user`\* - Get user's tickets
- `GET /api/tickets/event/{eventId}`\* - Get event's tickets (admin only)
- `DELETE /api/tickets/{ticketId}/cancel`\* - Cancel ticket
- `DELETE /api/tickets/{ticketId}/cancel/{userId}`\* - Cancel user's ticket (admin only)

## 🧪 Complete Test Flow Guide

This section provides a comprehensive testing workflow that demonstrates all application features.

### Prerequisites

```bash
# Set base URL for convenience
export BASE_URL="http://localhost:8081/event-ticket-1.0-SNAPSHOT/api"

# Ensure server is running
docker ps | grep event-ticket-app
```

---

## 📝 Phase 1: User Flow Testing

_Tests marked with \* require a token_

### 1.1 User Registration

```bash
# Register Admin User
curl -X POST $BASE_URL/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@test.com","password":"admin123","role":"admin"}'
# Expected: HTTP 201, user created

# Register Admin User 2 (for event management)
curl -X POST $BASE_URL/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin2","email":"admin2@test.com","password":"admin123","role":"admin"}'
# Expected: HTTP 201, user created

# Register Regular Users
curl -X POST $BASE_URL/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"user123","role":"user"}'

curl -X POST $BASE_URL/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user2","password":"user123","role":"user"}'

curl -X POST $BASE_URL/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user3","password":"user123","role":"user"}'
```

### 1.2 User Registration Error Testing

```bash
# Test duplicate username (should fail)
curl -X POST $BASE_URL/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"different","role":"user"}'
# Expected: HTTP 400, "User with username admin already exists"

# Test empty username (should fail)
curl -X POST $BASE_URL/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"","password":"password123","role":"user"}'
# Expected: HTTP 400, "Username is required"
```

### 1.3 User Login Testing

```bash
# Successful login
curl -X POST $BASE_URL/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
# Expected: HTTP 200, JWT token returned

# Failed login - wrong password
curl -X POST $BASE_URL/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrongpassword"}'
# Expected: HTTP 200, null response

# Failed login - non-existent user
curl -X POST $BASE_URL/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"nonexistent","password":"password"}'
# Expected: HTTP 200, null response
```

### 1.4 View Users

```bash
# Get all users*
curl -X GET $BASE_URL/users
# Expected: HTTP 200, array of all registered users
```

---

## 🎪 Phase 2: Event Flow Testing

### 2.1 Event Creation

```bash
# Create Large Event (100 tickets)
curl -X POST $BASE_URL/events \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Tech Innovation Summit 2025",
    "description":"Annual technology conference with industry leaders",
    "venue":"Grand Convention Center",
    "eventDate":"2025-12-15T09:00:00",
    "ticketPrice":150.00,
    "maxTickets":100
  }'
# Expected: HTTP 201, event created with availableTickets=100

# Create Medium Event (50 tickets)
curl -X POST $BASE_URL/events \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Summer Music Festival",
    "description":"3-day outdoor music festival featuring top artists",
    "venue":"City Park Amphitheater",
    "eventDate":"2025-08-20T18:00:00",
    "ticketPrice":75.00,
    "maxTickets":50
  }'
# Expected: HTTP 201, event created with availableTickets=50

# Create Small Event (20 tickets)
curl -X POST $BASE_URL/events \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Shakespeare in the Park",
    "description":"Classic theater performance under the stars",
    "venue":"Central Park Theater",
    "eventDate":"2025-07-25T19:30:00",
    "ticketPrice":45.00,
    "maxTickets":20
  }'
# Expected: HTTP 201, event created with availableTickets=20

# Create Very Limited Event (5 tickets)
curl -X POST $BASE_URL/events \
  -H "Content-Type: application/json" \
  -d '{
    "name":"VIP Cooking Masterclass",
    "description":"Exclusive cooking session with celebrity chef",
    "venue":"Private Kitchen Studio",
    "eventDate":"2025-10-10T14:00:00",
    "ticketPrice":200.00,
    "maxTickets":5
  }'
# Expected: HTTP 201, event created with availableTickets=5
```

### 2.2 Event Creation Error Testing

```bash
# Test past date event (should fail)
curl -X POST $BASE_URL/events \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Past Event",
    "description":"This should fail",
    "venue":"Nowhere",
    "eventDate":"2020-01-01T10:00:00",
    "ticketPrice":50.00,
    "maxTickets":10
  }'
# Expected: HTTP 400, "Event date must be in the future"

# Test negative price (should fail)
curl -X POST $BASE_URL/events \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Free Event",
    "description":"Negative price test",
    "venue":"Test Venue",
    "eventDate":"2025-12-01T10:00:00",
    "ticketPrice":-10.00,
    "maxTickets":10
  }'
# Expected: HTTP 400, "Ticket price must be non-negative"

# Test zero max tickets (should fail)
curl -X POST $BASE_URL/events \
  -H "Content-Type: application/json" \
  -d '{
    "name":"No Tickets Event",
    "description":"Zero tickets test",
    "venue":"Test Venue",
    "eventDate":"2025-12-01T10:00:00",
    "ticketPrice":50.00,
    "maxTickets":0
  }'
# Expected: HTTP 400, "Max tickets must be positive"

# Test empty name (should fail)
curl -X POST $BASE_URL/events \
  -H "Content-Type: application/json" \
  -d '{
    "name":"",
    "description":"Empty name test",
    "venue":"Test Venue",
    "eventDate":"2025-12-01T10:00:00",
    "ticketPrice":50.00,
    "maxTickets":10
  }'
# Expected: HTTP 400, "Event name is required"
```

### 2.3 Event Retrieval Testing

```bash
# Get all events
curl -X GET $BASE_URL/events
# Expected: HTTP 200, array of all events ordered by date

# Get specific event
curl -X GET $BASE_URL/events/1
# Expected: HTTP 200, Tech Innovation Summit details

# Get non-existent event (should fail)
curl -X GET $BASE_URL/events/999
# Expected: HTTP 404, "Event not found with id: 999"

# Get available events only
curl -X GET $BASE_URL/events/available
# Expected: HTTP 200, events with availableTickets > 0 and future dates


```

---

## 🎫 Phase 3: Ticket Flow Testing

### 3.1 Successful Ticket Booking

```bash
# Test 1: Single ticket booking
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":3,"eventId":1,"quantity":1}'
# Expected: HTTP 201, ticket created for user1 at Tech Summit
# Tech Summit available tickets: 99

# Test 2: Multiple ticket booking
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":4,"eventId":2,"quantity":3}'
# Expected: HTTP 201, 3 tickets created for user2 at Music Festival
# Music Festival available tickets: 47

# Test 3: Maximum allowed booking (5 tickets)
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":5,"eventId":1,"quantity":5}'
# Expected: HTTP 201, 5 tickets created for user3 at Tech Summit
# Tech Summit available tickets: 94

# Test 4: Book for limited event
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":6,"eventId":4,"quantity":2}'
# Expected: HTTP 201, 2 tickets created for user4 at VIP Masterclass
# VIP Masterclass available tickets: 3

# Test 5: Book remaining tickets for small event
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":7,"eventId":4,"quantity":3}'
# Expected: HTTP 201, 3 tickets created for user5 at VIP Masterclass
# VIP Masterclass available tickets: 0 (SOLD OUT)
```

### 3.2 Ticket Booking Error Testing

```bash
# Test 6: Duplicate booking (should fail)
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":3,"eventId":1,"quantity":1}'
# Expected: HTTP 400, "You already have a ticket for this event"

# Test 7: Excessive quantity (should fail)
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":6,"eventId":2,"quantity":10}'
# Expected: HTTP 400, "Maximum 5 tickets per booking"

# Test 8: Book for sold out event (should fail)
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":6,"eventId":4,"quantity":1}'
# Expected: HTTP 400, "Only 0 tickets available"

# Test 9: Invalid user ID (should fail)
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":999,"eventId":1,"quantity":1}'
# Expected: HTTP 400, "User not found"

# Test 10: Invalid event ID (should fail)
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":3,"eventId":999,"quantity":1}'
# Expected: HTTP 400, "Event not found"

# Test 11: Zero quantity (should fail)
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":3,"eventId":2,"quantity":0}'
# Expected: HTTP 400, "Quantity must be positive"

# Test 12: Negative quantity (should fail)
curl -X POST $BASE_URL/tickets/book \
  -H "Content-Type: application/json" \
  -d '{"userId":3,"eventId":2,"quantity":-1}'
# Expected: HTTP 400, "Quantity must be positive"
```

### 3.3 Ticket Retrieval Testing

_A token is required for all these requests_

```bash
# Get all tickets
curl -X GET $BASE_URL/tickets
# Expected: HTTP 200, array of all tickets with booking details

# Get tickets by user
curl -X GET $BASE_URL/tickets/user/3
# Expected: HTTP 200, tickets for user1 (should have 1 ticket)

# Get tickets by event
curl -X GET $BASE_URL/tickets/event/1
# Expected: HTTP 200, all tickets for Tech Summit (should have 6 tickets total)

curl -X GET $BASE_URL/tickets/event/2
# Expected: HTTP 200, all tickets for Music Festival (should have 3 tickets)

curl -X GET $BASE_URL/tickets/event/4
# Expected: HTTP 200, all tickets for VIP Masterclass (should have 5 tickets)

# Get tickets for non-existent user
curl -X GET $BASE_URL/tickets/user/999
# Expected: HTTP 200, empty array

# Get tickets for non-existent event
curl -X GET $BASE_URL/tickets/event/999
# Expected: HTTP 200, empty array
```

---

## ❌ Phase 4: Ticket Cancellation Testing

### 4.1 Successful Cancellation

```bash
# Cancel user1's ticket (ticket ID should be 1)
curl -X DELETE $BASE_URL/tickets/1/cancel/3
# Expected: HTTP 200, "Ticket cancelled successfully"
# Tech Summit available tickets should increase to 95

# Verify cancellation worked
curl -X GET $BASE_URL/tickets/user
# Expected: HTTP 200, ticket status should be "CANCELLED"

curl -X GET $BASE_URL/events/1
# Expected: HTTP 200, availableTickets should be 95
```

### 4.2 Cancellation Error Testing

```bash
# Try to cancel someone else's ticket
curl -X DELETE $BASE_URL/tickets/2/cancel/3
# Expected: HTTP 400, "You can only cancel your own tickets"

# Try to cancel already cancelled ticket
curl -X DELETE $BASE_URL/tickets/1/cancel/3
# Expected: HTTP 400, "Ticket is already cancelled"

# Try to cancel non-existent ticket
curl -X DELETE $BASE_URL/tickets/999/cancel/3
# Expected: HTTP 400, "Ticket not found"

# Try to cancel with wrong user ID
curl -X DELETE $BASE_URL/tickets/2/cancel/999
# Expected: HTTP 400, "You can only cancel your own tickets"
```

---

## 🔍 Phase 5: Final State Verification

### 5.1 Verify Event States

```bash
# Check all events and their available tickets
curl -X GET $BASE_URL/events

# Expected final state:
# Event 1 (Tech Summit): 95 available (100 - 6 + 1 cancelled)
# Event 2 (Music Festival): 47 available (50 - 3)
# Event 3 (Shakespeare): 20 available (unchanged)
# Event 4 (VIP Masterclass): 0 available (5 - 5, SOLD OUT)
```

### 5.2 Verify Ticket States

```bash
# Check all tickets and their statuses
curl -X GET $BASE_URL/tickets

# Expected: Multiple tickets with various statuses
# - Ticket 1: status = "CANCELLED"
# - Other tickets: status = "CONFIRMED"
```

### 5.3 Verify Available Events

```bash
# Get only available events
curl -X GET $BASE_URL/events/available

# Expected: Events 1, 2, 3 (Event 4 should be excluded as it's sold out)
```

---

## 📊 Test Results Summary

After completing all tests, you should have:

### Users Created: 5

- admin (ID: 1) - Main admin
- admin2 (ID: 2) - Event manager admin
- user1 (ID: 3)
- user2 (ID: 4)
- user3 (ID: 5)

### Events Created: 4

- Tech Summit: 95/100 tickets available
- Music Festival: 47/50 tickets available
- Shakespeare: 20/20 tickets available
- VIP Masterclass: 0/5 tickets available (SOLD OUT)

### Tickets Booked: 14 total

- 1 cancelled ticket (returned to pool)
- 13 confirmed tickets
- Proper quantity and availability tracking

### Error Scenarios Tested: 15+

- All validation rules working correctly
- Proper error messages returned
- Security measures in place

---

## 🎯 Success Criteria

Your application passes all tests if:

✅ **User Flow**: Registration, login, and validation work correctly
✅ **Event Flow**: Creation, retrieval, and validation work correctly
✅ **Ticket Flow**: Booking, retrieval, and cancellation work correctly
✅ **Error Handling**: All invalid scenarios return proper error messages
✅ **Data Integrity**: Ticket counts and availability are accurate
✅ **Security**: No SQL injection, passwords hashed, input validated

---
