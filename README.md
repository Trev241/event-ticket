# Event Ticket Application

Jakarta EE 10 application using Payara Micro and PostgreSQL.

## Compatible Versions
- **Java**: 17
- **Jakarta EE**: 10.0.0
- **Payara Micro**: 6.2024.9
- **PostgreSQL**: 15

## Quick Start
```bash
docker-compose up --build
```

## Access
- App: http://localhost:8080
- API: http://localhost:8080/api

## Test
```bash
# Register user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","role":"admin"}'

# Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```
