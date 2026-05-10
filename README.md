# Train Ticketing & Route Management System

A full-stack Java application for managing train schedules, automated route finding, and ticket bookings.  
Built as a Java internship assignment.

---

## Tech Stack

- Backend: Java 21, Spring Boot 3, Spring Data JPA, MySQL 8, Gradle
- Frontend: Angular (standalone components, HttpClient)
- Email: JavaMail with Mailtrap (SMTP sandbox)
- Infrastructure: Docker Compose for backend + database

---

## How to Run

### 1. Configure Email (Mailtrap)

In `src/main/resources/application.properties` set your Mailtrap credentials to see emails in action:

```properties
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=YOUR_USERNAME
spring.mail.password=YOUR_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

You can use the example Mailtrap credentials from the code while testing, or replace them with your own.

### 2. Start Backend and Database (Docker)

From the project root:

```bash
docker-compose up --build
```

This starts:

- MySQL with seeded data from `schema.sql`

- The Spring Boot application runs on the host machine and connects to the MySQL container via localhost:3306.

### 2.1 Start the Backend (Spring Boot)

Option A: Run the main class from your IDE:

src/main/java/com/siemens/train/TrainTicketingApplication.java

Option B: Use the Gradle wrapper in the terminal: ./gradlew bootRun

Backend Endpoints:
- REST API base (used by Angular and Postman): `http://localhost:8080`
- Swagger documentation: `http://localhost:8080/swagger-ui.html`

(The backend does not serve any HTML UI; it only provides the REST API and Swagger.)

### 3. Start Frontend (Angular)

In a separate terminal:

```bash
cd train-frontend
npm install
ng serve --proxy-config proxy.conf.json
```

Angular dev server:

- Web interface: `http://localhost:4200`

The Angular app uses a proxy configuration to forward API calls to the Spring Boot backend on `localhost:8080`.

## Predefined Seed Data

The database is automatically seeded on first startup using `schema.sql`.

**Stations**

| ID | Name                 | City        |
|----|----------------------|------------|
| 1  | Cluj-Napoca Central | Cluj-Napoca |
| 2  | Brasov Central       | Brasov      |
| 3  | Bucuresti Nord       | Bucuresti   |
| 4  | Sinaia               | Sinaia      |
| 5  | Predeal              | Predeal     |

**Routes & Trains**

| Train        | ID | Capacity | Route                                  |
|--------------|----|----------|----------------------------------------|
| IC 123       | 1  | 200      | Cluj → Brasov → Bucuresti             |
| IR 456       | 2  | 150      | Brasov → Sinaia → Predeal → Bucuresti |
| REGIO 789    | 3  | 100      | Brasov → Sinaia → Predeal → Bucuresti |

Train stops (arrival/departure times and stop order) are also seeded in `schema.sql`, providing realistic sample schedules.

---

## Key Technical Features

Dijkstra-based Route Finding: Finds direct journeys and connections with transfers, enforcing a mandatory 5-minute minimum transfer window.

Safe Ticket Booking: Prevents overbooking by checking train capacity and validates that the departure station precedes the arrival station on the route.

Async Notifications: Confirmation and delay emails are sent asynchronously using CompletableFuture to ensure fast API responses.

Role Management: Features a frontend profile selector (Customer/Admin) to toggle between booking and management functionalities.

## Supported Functionalities (With Example I/O)

A Postman collection (`train.postman_collection.json`) is included in the repository.  
It contains ready-made requests that demonstrate the following core features required by the assignment.

### a) Booking Tickets

**Create a valid booking**

```http
POST /api/bookings
Content-Type: application/json

{
  "trainId": 1,
  "departureStationId": 1,
  "arrivalStationId": 3,
  "customerEmail": "passenger@example.com",
  "numberOfSeats": 2
}
```

- Result: `201 Created`, booking is stored.
- Side effect: A booking confirmation email is sent to `passenger@example.com`.

**Overbooking protection**

Attempt to book more seats than the train capacity allows:

```http
POST /api/bookings
Content-Type: application/json

{
  "trainId": 1,
  "departureStationId": 1,
  "arrivalStationId": 3,
  "customerEmail": "passenger@example.com",
  "numberOfSeats": 300
}
```

Typical response:

```json
{
  "error": "Not enough seats available. Available: 198"
}
```

**Invalid station order**

Attempt to book from a station that comes after the arrival station on that train’s route:

```http
POST /api/bookings
Content-Type: application/json

{
  "trainId": 1,
  "departureStationId": 3,
  "arrivalStationId": 1,
  "customerEmail": "passenger@example.com",
  "numberOfSeats": 1
}
```

Response:

```json
{
  "error": "Departure station must come before arrival station"
}
```

---

### b) Finding Routes Between Stations

**Direct connection (no transfer)**

```http
GET /api/routes/find?from=1&to=3&after=2026-05-10T07:00:00
```

Example response:

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

Same `trainName` across segments indicates a direct journey without changing trains.

**Route with transfer**

```http
GET /api/routes/find?from=1&to=4&after=2026-05-10T07:00:00
```

Example response:

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

Here the journey changes from train `IC 123` to `REGIO 789` at Brasov, with a safe transfer window.

**No possible route**

```http
GET /api/routes/find?from=5&to=1&after=2026-05-10T07:00:00
```

Example response:

```json
{
  "error": "No route found between the given stations"
}
```

---

### c) Administrator Operations

All administrator operations are available both through:

- the Angular admin view, and
- the REST API (see Swagger UI).

**View bookings for a train**

```http
GET /api/bookings/train/1
```

Returns all bookings made for train with ID 1, including passenger email, seat count, and booking date.

**Mark a train as delayed**

```http
POST /api/trains/1/delay
```

- Sets `delayed = true` for train ID 1.
- Sends a delay notification email to every customer that has a booking on this train.

**Manage trains**

- Create a train: `POST /api/trains`
- Update a train: `PUT /api/trains/{id}`
- Delete a train: `DELETE /api/trains/{id}`

**Manage routes and stations**

- Create a route: `POST /api/routes`
- Add station to route: `POST /api/routes/{routeId}/stations/{stationId}`
- Remove station from route: `DELETE /api/routes/{routeId}/stations/{stationId}`
- Delete a route: `DELETE /api/routes/{id}`

---

## Testing

Postman: Import the `train.postman_collection.json` file to test all API endpoints (booking, route search, and admin CRUD).

## Security:
This is a Proof-of-Concept focused on business logic. Full Spring Security/JWT is omitted in favor of a simplified frontend role selection. 
In a production scenario, the same features would be secured using Spring Security, JWT-based authentication, and role-based authorization.

## Problem 2 (Optional): Frontend for the REST API

 Problem:
A pure REST API is hard to use for normal users; tools like Postman are only comfortable for developers.

 Solution: 
I added a separate Angular Single Page Application (SPA) that works as a client for the Java backend.

- A profile selector switches between customer booking and admin management views.
- A proxy config (`proxy.conf.json`) forwards requests to `http://localhost:8080`, avoiding CORS issues during development.