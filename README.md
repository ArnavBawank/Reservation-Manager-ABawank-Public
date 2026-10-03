# Reservation Manager CS180

**Author: Arnav Bawankule**

## Overview

Restaurant reservation manager with static seating.

- Phase 1: file-backed authentication, seating, reservations, rules, and synchronized stores
- Phase 2: socket server with a thread per client, line protocol, and stateless CLI client
- Phase 3: full GUI with booking panel, reservations view, admin panel, and seating chart display

## Important

Some unit tests may fail on Vocareum because graphical items do not run properly. Running the unit tests locally works as expected.

---

## Features

- Authentication and sessions with file persistence
- List open tables, book and cancel reservations
- Admin hours, section locks, and table management
- Per-slot locking to prevent double booking
- Numeric 8-digit reservation IDs
- Server listens on port 8888
- Multi-client support with one thread per client
- GUI and CLI clients
- Dynamic seating chart
- Account deletion
- Pricing display
- Interface-driven architecture
- JUnit testing and mock objects

## Directory Layout

- **src/** - production code and tests
- **test/** - JUnit 4 tests and mock classes
- **data/** - persistent storage files
- **lib/** - JUnit and Hamcrest libraries
- **out/** - compiled `.class` files

### Services

`AuthService`, `BookingService`, `AvailabilityService`, `AdminService`, `Rules`, `MyReservationsService`

### Stores

`UserStore`, `SeatingStore`, `ReservationStore`, `ConfigStore`

### Models

`User`, `Table`, `Reservation`, `TimeSlot`, `Hours`, `Config`, `SectionLock`, `Status`

### Server

`ReservationServer`, `ClientHandler`, `SessionMap`

### Client

`ReservationClient`, `ClientMain`

### GUI

`GUIMain`, `ReservationGUI`, `LoginPanel`, `MainMenuPanel`, `BookingPanel`, `MyReservationsPanel`, `AdminPanel`, `SeatingChartPanel`

---

## Build

Compile all classes:

```bash
javac -d out -cp "lib/*" src/*.java test/*.java
```

## Run Quick Demos

```bash
java -cp "out:lib/*" CancelReservation
java -cp "out:lib/*" ViewPricing
```

## Run Server and GUI

### Server

```bash
java -cp "out:lib/*" ReservationServer
```

### GUI Client

```bash
java -cp "out:lib/*" GUIMain
```

### CLI Client

```bash
java -cp "out:lib/*" ClientMain
```

## Admin Access

Admin features are restricted to the admin account.

- **Username:** `admin`
- **Password:** `admin`

The Admin Panel allows setting hours, locking sections, and managing tables.

## Default Setup

### Hours

- Weekdays: 9:00 AM – 10:00 PM
- Weekends: 10:00 AM – 11:00 PM

### Tables

| Table | Capacity | Section |
|---|---:|---|
| T1-T2 | 2 | Main |
| T3-T4 | 4 | Main |
| T5 | 6 | Main |
| T6-T7 | 4 | Patio |
| T8 | 6 | Patio |
| T9 | 8 | Private |

### Pricing

- Standard Seating: $25/person
- Patio Seating: $30/person
- Private Room: $100 flat + $25/person

## Reference

### Client-to-server commands

```text
CREATE_USER email password
LOGIN email password
LOGOUT
LIST_OPEN yyyy-MM-dd HH:mm partySize
BOOK yyyy-MM-dd HH:mm partySize
CANCEL reservationId
LIST_MY_RESERVATIONS
ADMIN_SET_HOURS DAY HH:mm HH:mm
ADMIN_LOCK_SECTION yyyy-MM-dd HH:mm section
ADMIN_UPSERT_TABLE tableId capacity section
DELETE_USER
```

### Server responses

```text
OK ...
ERROR: message
```

## Thread Safety

- File stores are synchronized
- Booking uses per-slot locks
- Server uses one thread per client
- Client is stateless
- Configuration writes are synchronized
- Reservation IDs are unique 8-digit numbers

## GUI

The application includes:

- Login and account creation
- Main navigation menu
- Table search and booking
- Reservation management
- Admin controls
- Dynamic seating chart
- Pricing display
- Account deletion

### Design Choices

**CardLayout Navigation**

The GUI uses CardLayout to switch between login, menu, booking, reservation, and administrative views while maintaining application state.

**Thin Client Design**

The client handles user input and display while business logic and reservation validation remain on the server.

**Mock Objects**

Mock clients are used to test GUI components without requiring a running server.

**Interface-Driven Design**

GUI panels and model classes implement interfaces to support dependency injection and testing.

## Testing

The project includes JUnit tests covering:

- Authentication
- Availability
- Booking
- Reservation persistence
- Seating persistence
- Rules
- Administration
- Client/server communication
- GUI panels
- Models
- Helper classes

## Manual Testing

- Ran the GUI
- Tested administrative actions
- Tested valid and invalid inputs
- Verified protocol messages and server responses

## Author

**Arnav Bawankule**

CS180 — Reservation Manager
