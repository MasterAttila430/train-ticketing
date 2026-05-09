# Train Ticketing Application

A Spring Boot REST API for managing train routes, schedules, and bookings. Built as a Java internship assignment.

---

## Tech Stack

- Java 17, Spring Boot 3, MySQL 8, Gradle
- Email: JavaMail with Mailtrap (SMTP sandbox)
- Docker Compose for easy setup

---

## How to Run

```
git clone https://github.com/MasterAttila430/train-ticketing.git
cd train-ticketing
docker-compose up --build
```

Web UI:    http://localhost:8080
Swagger:   http://localhost:8080/swagger-ui.html

The database is seeded automatically on first startup with predefined stations, routes, and trains.

---

## Predefined Data

**Stations**

| ID | Name | City |
|----|------|------|
| 1 | Cluj-Napoca Central | Cluj-Napoca |
| 2 | Brasov Central | Brasov |
| 3 | Bucuresti Nord | Bucuresti |
| 4 | Sinaia | Sinaia |
| 5 | Predeal | Predeal |

**Routes & Trains**

| Train | Capacity | Route |
|-------|----------|-------|
| IC 123 (ID 1) | 200 | Cluj → Brasov → Bucuresti |
| IR 456 (ID 2) | 150 | Brasov → Sinaia → Predeal → Bucuresti |
| REGIO 789 (ID 3) | 100 | Brasov → Sinaia → Predeal → Bucuresti |

---

## Functionality & Examples

The full Postman collection (`train.postman_collection.json`) is included in the repository with saved responses for all requests.

---

### a) Booking Tickets

#### Successful Booking

**Input:**
```
POST /api/bookings
Content-Type: application/json

{
  "trainId": 1,
  "departureStationId": 1,
  "arrivalStationId": 3,
  "customerEmail": "customer@train.com",
  "numberOfSeats": 2
}
```

**Output (`201 Created`):**
```json
{
  "id": 1,
  "train": { "id": 1, "name": "IC 123", "capacity": 200, "delayed": false },
  "departureStation": { "id": 1, "name": "Cluj-Napoca Central", "city": "Cluj-Napoca" },
  "arrivalStation": { "id": 3, "name": "Bucuresti Nord", "city": "Bucuresti" },
  "customerEmail": "customer@train.com",
  "numberOfSeats": 2,
  "bookingDate": "2026-05-09T13:00:48"
}
```

A confirmation email is sent to `customer@train.com` after the booking is created.

---

#### Overbooking Prevention

**Input:**
```
POST /api/bookings
Content-Type: application/json

{
  "trainId": 1,
  "departureStationId": 1,
  "arrivalStationId": 3,
  "customerEmail": "customer@train.com",
  "numberOfSeats": 999
}
```

**Output (`400 Bad Request`):**
```json
{
  "error": "Not enough seats available. Available: 198"
}
```

---

#### Invalid Station Order

Departure station must come before arrival station on the route.

**Input:**
```
POST /api/bookings
Content-Type: application/json

{
  "trainId": 1,
  "departureStationId": 3,
  "arrivalStationId": 1,
  "customerEmail": "customer@train.com",
  "numberOfSeats": 1
}
```

**Output (`400 Bad Request`):**
```json
{
  "error": "Departure station must come before arrival station"
}
```

---

### b) Finding Routes

#### Direct Connection

**Input:**
```
GET /api/routes/find?from=1&to=3&after=2025-01-01T07:00:00
```

- `from=1` — Cluj-Napoca Central
- `to=3` — Bucuresti Nord
- `after` — earliest departure time

**Output (`200 OK`):**
```json
[
  {
    "trainName": "IC 123",
    "fromStation": "Cluj-Napoca Central",
    "departure": "2026-05-10T08:00:00",
    "toStation": "Brasov Central",
    "arrival": "2026-05-10T10:30:00"
  },
  {
    "trainName": "IC 123",
    "fromStation": "Brasov Central",
    "departure": "2026-05-10T10:45:00",
    "toStation": "Bucuresti Nord",
    "arrival": "2026-05-10T13:00:00"
  }
]
```

The same `trainName` across all segments means it is a direct trip with no transfer.

---

#### Connection with Transfer

**Input:**
```
GET /api/routes/find?from=1&to=4&after=2025-01-01T07:00:00
```

- `from=1` — Cluj-Napoca Central
- `to=4` — Sinaia
- No direct train exists; the application finds a transfer at Brasov.

**Output (`200 OK`):**
```json
[
  {
    "trainName": "IC 123",
    "fromStation": "Cluj-Napoca Central",
    "departure": "2026-05-10T08:00:00",
    "toStation": "Brasov Central",
    "arrival": "2026-05-10T10:30:00"
  },
  {
    "trainName": "REGIO 789",
    "fromStation": "Brasov Central",
    "departure": "2026-05-10T11:00:00",
    "toStation": "Sinaia",
    "arrival": "2026-05-10T11:45:00"
  }
]
```

When `trainName` changes between segments, a transfer is required at the intermediate station (Brasov Central in this case).

---

#### No Connection Found

**Input:**
```
GET /api/routes/find?from=5&to=1&after=2025-01-01T07:00:00
```

- `from=5` — Predeal
- `to=1` — Cluj-Napoca Central
- No train runs in this direction.

**Output (`404 Not Found`):**
```json
{
  "error": "No route found between the given stations"
}
```

---

### c) Admin Operations

#### View Bookings for a Train

**Input:**
```
GET /api/bookings/train/1
```

**Output (`200 OK`):**
```json
[
  {
    "id": 1,
    "train": { "id": 1, "name": "IC 123", "capacity": 200 },
    "departureStation": { "id": 1, "name": "Cluj-Napoca Central", "city": "Cluj-Napoca" },
    "arrivalStation": { "id": 3, "name": "Bucuresti Nord", "city": "Bucuresti" },
    "customerEmail": "customer@train.com",
    "numberOfSeats": 2,
    "bookingDate": "2026-05-09T13:00:49"
  }
]
```

---

#### Mark a Train as Delayed

Sets the train's `delayed` flag to `true` and sends a notification email to every customer who has a booking on that train.

**Input:**
```
POST /api/trains/1/delay
```

**Output (`200 OK`):**
```json
{
  "id": 1,
  "name": "IC 123",
  "capacity": 200,
  "delayed": true,
  "route": { "id": 1, "name": "Cluj - Bucuresti" }
}
```

All customers with bookings on IC 123 receive a delay notification email.

---

#### Add a New Train

**Input:**
```
POST /api/trains
Content-Type: application/json

{
  "name": "TEST-1",
  "capacity": 50,
  "routeId": 1
}
```

**Output (`201 Created`):**
```json
{
  "id": 4,
  "name": "TEST-1",
  "capacity": 50,
  "delayed": false,
  "route": { "id": 1, "name": "Cluj - Bucuresti" }
}
```

---

#### Update a Train

**Input:**
```
PUT /api/trains/4
Content-Type: application/json

{
  "name": "TEST-1-MODIFIED",
  "capacity": 80,
  "routeId": 1
}
```

**Output (`200 OK`):**
```json
{
  "id": 4,
  "name": "TEST-1-MODIFIED",
  "capacity": 80,
  "delayed": false,
  "route": { "id": 1, "name": "Cluj - Bucuresti" }
}
```

---

#### Delete a Train

**Input:**
```
DELETE /api/trains/4
```

**Output: `204 No Content`**

---

#### Add a New Route

**Input:**
```
POST /api/routes
Content-Type: application/json

{
  "name": "Test Route",
  "stations": []
}
```

**Output (`201 Created`):**
```json
{
  "id": 3,
  "name": "Test Route",
  "stations": []
}
```

---

#### Add a Station to a Route

**Input:**
```
POST /api/routes/3/stations/1
```

**Output (`200 OK`):**
```json
{
  "id": 3,
  "name": "Test Route",
  "stations": [
    { "id": 1, "name": "Cluj-Napoca Central", "city": "Cluj-Napoca" }
  ]
}
```

---

#### Remove a Station from a Route

**Input:**
```
DELETE /api/routes/3/stations/1
```

**Output (`200 OK`):** Updated route object without the removed station.

---

#### Delete a Route

**Input:**
```
DELETE /api/routes/3
```

**Output: `204 No Content`**

---

## Web UI

A minimal web interface is available at `http://localhost:8080/index.html`.

- **Customer view:** Search for connections between stations, set number of passengers, and book tickets directly.
- **Admin view:** Switch the role selector to "Admin" to manage trains, routes, and stations, view bookings per train, and trigger delay notifications.

---

## Email Notifications

Two emails are sent automatically:

1. **Booking confirmation** — sent after every successful booking to the customer's email address.
2. **Delay notification** — sent to all customers booked on a train when an admin marks it as delayed.

To configure your own SMTP, update `src/main/resources/application.properties`:

```properties
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=YOUR_USERNAME
spring.mail.password=YOUR_PASSWORD
```
