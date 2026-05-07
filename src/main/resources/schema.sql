CREATE DATABASE IF NOT EXISTS train_ticketing;
USE train_ticketing;

CREATE TABLE IF NOT EXISTS stations (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS routes (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Junction table: ordered list of stations in a route
CREATE TABLE IF NOT EXISTS route_stations (
    route_id    BIGINT NOT NULL,
    station_id  BIGINT NOT NULL,
    stop_order  INT    NOT NULL,
    PRIMARY KEY (route_id, stop_order),
    FOREIGN KEY (route_id)   REFERENCES routes(id)   ON DELETE CASCADE,
    FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trains (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    capacity INT          NOT NULL,
    delayed  BOOLEAN      NOT NULL DEFAULT FALSE,
    route_id BIGINT       NOT NULL,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE RESTRICT
);

-- TrainStop: which train stops at which station and when
CREATE TABLE IF NOT EXISTS train_stops (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_id         BIGINT   NOT NULL,
    station_id       BIGINT   NOT NULL,
    arrival_time     DATETIME,
    departure_time   DATETIME,
    stop_order       INT      NOT NULL,
    FOREIGN KEY (train_id)   REFERENCES trains(id)   ON DELETE CASCADE,
    FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS app_users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    role          ENUM('ADMIN', 'CUSTOMER') NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_id            BIGINT       NOT NULL,
    departure_station_id BIGINT      NOT NULL,
    arrival_station_id  BIGINT       NOT NULL,
    customer_email      VARCHAR(255) NOT NULL,
    number_of_seats     INT          NOT NULL,
    booking_date        DATETIME     NOT NULL,
    FOREIGN KEY (train_id)             REFERENCES trains(id)   ON DELETE RESTRICT,
    FOREIGN KEY (departure_station_id) REFERENCES stations(id) ON DELETE RESTRICT,
    FOREIGN KEY (arrival_station_id)   REFERENCES stations(id) ON DELETE RESTRICT
);

-- Sample data
INSERT INTO stations (name, city) VALUES
    ('Cluj-Napoca Central', 'Cluj-Napoca'),
    ('Brasov Central',      'Brasov'),
    ('Bucuresti Nord',      'Bucuresti'),
    ('Sinaia',              'Sinaia'),
    ('Predeal',             'Predeal');

INSERT INTO routes (name) VALUES
    ('Cluj - Bucuresti'),
    ('Brasov - Bucuresti');

INSERT INTO route_stations (route_id, station_id, stop_order) VALUES
    (1, 1, 1),
    (1, 2, 2),
    (1, 3, 3),
    (2, 2, 1),
    (2, 4, 2),
    (2, 5, 3),
    (2, 3, 4);

INSERT INTO trains (name, capacity, delayed, route_id) VALUES
    ('IC 123', 200, FALSE, 1),
    ('IR 456', 150, FALSE, 2);

INSERT INTO train_stops (train_id, station_id, arrival_time, departure_time, stop_order) VALUES
    (1, 1, NULL,                  '2025-01-01 08:00:00', 1),
    (1, 2, '2025-01-01 10:30:00', '2025-01-01 10:45:00', 2),
    (1, 3, '2025-01-01 13:00:00', NULL,                  3),
    (2, 2, NULL,                  '2025-01-01 09:00:00', 1),
    (2, 4, '2025-01-01 09:45:00', '2025-01-01 09:50:00', 2),
    (2, 5, '2025-01-01 10:15:00', '2025-01-01 10:20:00', 3),
    (2, 3, '2025-01-01 11:00:00', NULL,                  4);

INSERT INTO app_users (username, password_hash, email, role) VALUES
    ('admin', '$2a$10$placeholder_hash_admin',    'admin@train.com',    'ADMIN'),
    ('user1', '$2a$10$placeholder_hash_customer', 'customer@train.com', 'CUSTOMER');