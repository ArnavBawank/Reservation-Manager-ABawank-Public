# Reservation Manager CS180

**Krishna Vijay — Submitted Code on Vocareum workspace**

**Daniel Gong - Submitted Presentation (Video) on Brightspace**

**Arnav Bawankule - Submitted Report (Document) on Brightspace**

## Overview
* Restaurant reservation manager with static seating
* Phase 1: file backed auth, seating, reservations, rules, synchronized stores
* Phase 2: socket server (thread per client) with line protocol and stateless CLI client, all logic and persistence on the server
* Phase 3: full GUI with booking panel, reservations view, admin panel, and seating chart display

## IMPORTANT
Some unit tests fail on Vocareum because it does not let graphical items run properly. Running unit tests locally has no issues.

## Features
* Auth and sessions with file persistence
* List open tables, book and cancel, admin hours, locks, and table upsert
* Per slot locking to avoid double booking, reservation IDs are numeric 8 digit strings
* Server listens on 8888, one thread per client, client only relays commands

## Directory Layout
* **src/** - all production code and tests
  * **Services:** `AuthService`, `BookingService`, `AvailabilityService`, `AdminService`, `Rules`, `MyReservationsService` (+ Simple implementations)
  * **Stores:** `UserStore`, `SeatingStore`, `ReservationStore`, `ConfigStore` (+ File implementations)
  * **Models:** `User`, `Table`, `Reservation`, `TimeSlot`, `Hours`, `Config`, `SectionLock`, `Status`
  * **Model Interfaces:** `UserInterface`, `TableInterface`, `ReservationInterface`, `TimeSlotInterface`, `HoursInterface`, `ConfigInterface`, `SectionLockInterface`
  * **Server:** `ReservationServer`, `ClientHandler`, `SessionMap`, `ServerInterface`, `ClientHandlerInterface`
  * **Client:** `ReservationClient`, `ReservationClientInterface`, `ClientMain`
  * **GUI:** `GUIMain`, `ReservationGUI`, `LoginPanel`, `MainMenuPanel`, `BookingPanel`, `MyReservationsPanel`, `AdminPanel`, `SeatingChartPanel`
  * **GUI Interfaces:** `ReservationGUIInterface`, `LoginPanelInterface`, `MainMenuPanelInterface`, `BookingPanelInterface`, `MyReservationsPanelInterface`, `AdminPanelInterface`, `SeatingChartPanelInterface`
  * **Helpers:** `BookingCommandHelper`, `AuthCommandHelper`, `FileUtil` (+ interfaces)
* **test/** - all JUnit 4 tests and mocks (`*Test.java`, `Mock*.java`)
* **data/** - persistent storage files (`users.txt`, `reservations.txt`, `seating.txt`, `config.txt`)
* **lib/** - JUnit and Hamcrest jars
* **out/** - compiled `.class` files

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
Server (port 8888 default):
```bash
java -cp "out:lib/*" ReservationServer
```
GUI Client:
```bash
java -cp "out:lib/*" GUIMain
```
CLI Client (prompts for host and port):
```bash
java -cp "out:lib/*" ClientMain
```

## Admin Access
Admin features are restricted to the admin account:
* **Username:** `admin`
* **Password:** `admin`

Log in with these credentials to access the Admin Panel for setting hours, locking sections, and managing tables.

## Default Setup
The system comes pre-configured with sample data:

**Hours (Mon-Sun):** 9am-10pm weekdays, 10am-11pm weekends

**Tables:**
| Table | Capacity | Section |
|-------|----------|---------|
| T1-T2 | 2 | Main |
| T3-T4 | 4 | Main |
| T5 | 6 | Main |
| T6-T7 | 4 | Patio |
| T8 | 6 | Patio |
| T9 | 8 | Private |

**Pricing (displayed in GUI, reservations are free):**
* Standard Seating: $25/person
* Patio Seating: $30/person
* Private Room: $100 flat + $25/person

## Reference
* Client to server commands (single line):
  * `CREATE_USER email password`
  * `LOGIN email password`
  * `LOGOUT`
  * `LIST_OPEN yyyy-MM-dd HH:mm partySize`
  * `BOOK yyyy-MM-dd HH:mm partySize`
  * `CANCEL reservationId`
  * `LIST_MY_RESERVATIONS`
  * `ADMIN_SET_HOURS DAY HH:mm HH:mm` (DAY accepts short or full names)
  * `ADMIN_LOCK_SECTION yyyy-MM-dd HH:mm section`
  * `ADMIN_UPSERT_TABLE tableId capacity section`
  * `DELETE_USER` (deletes current logged-in user's account)
* Server responses:
  * Success: `OK ...`
  * Error: `ERROR: message`

## Manual IO Test Script
1. Start server, then open one or more clients (CLI prompts for host and port).
2. Paste these commands in order:
   * `CREATE_USER alice@example.com p`
   * `LOGIN alice@example.com p`
   * `LIST_OPEN 2025-12-01 18:00 4`
   * `BOOK 2025-12-01 18:00 4`
   * `LIST_MY_RESERVATIONS`
   * In another client: `LOGIN alice@example.com p` then `LIST_OPEN 2025-12-01 18:00 4` (table should be gone)
   * Back to first: `CANCEL <reservationId>` (use id from BOOK)
   * Second client: `LIST_OPEN 2025-12-01 18:00 4` (table should be back)
   * Admin: `ADMIN_SET_HOURS MON 09:00 21:00`, `ADMIN_LOCK_SECTION 2025-12-01 18:00 Main`, `ADMIN_UPSERT_TABLE 7 4 Patio`
3. Success responses start with `OK ...`; errors start with `ERROR: ...`.

## JUnit
Run all test suites (144 tests):
```bash
java -cp "out:lib/*" org.junit.runner.JUnitCore \
  AuthServiceTest AvailabilityServiceTest BookingConcurrencyTest \
  ReservationStorePersistenceTest SeatingStorePersistenceTest RulesTest \
  AdminServiceTest BookingCommandHelperTest MyReservationsServiceTest \
  ReservationClientTest ReservationServerTest AdminServiceSimpleUnitTest \
  RulesSimpleUnitTest LoginPanelTest BookingPanelTest MyReservationsPanelTest \
  AdminPanelTest MainMenuPanelTest ReservationGUITest SeatingChartPanelTest \
  UserTest TableTest ReservationTest TimeSlotTest HoursTest ConfigTest \
  SectionLockTest FileUtilTest AuthCommandHelperTest
```

## Data Files
* Users: `data/users.txt` as `id,email,password`
* Reservations: `data/reservations.txt` as `id,userId,tableId,partySize,YYYY-MM-DD,HH:mm,STATUS`
* Seating: `data/seating.txt` as `tableId,capacity,section`
* Config: `data/config.txt` as hours `HOURS,DAY,HH:mm,HH:mm` and locks `LOCK,YYYY-MM-DD,HH:mm,Section`
* Tests may clear `data/reservations.txt` and `data/config.txt` during setup and teardown, run tests before manual data entry if you want to keep manual data

## Thread Safety
* All file stores synchronized, booking uses per slot locks, server is thread per client, client is stateless
* Config writes synchronized in `FileConfigStore`, booking IDs are unique 8 digit numbers

## Class Notes
* Networking: `ServerInterface` (start and stop), `ReservationServer` (accept loop), `ClientHandler` (parses commands, calls services), `ReservationClient` (line sender), `ClientMain` (menu)
* Services: auth, availability, booking, admin, rules (each has interface and implementation; covered by tests)
* Stores: file based user, seating, reservation, config (synchronized)
* Helpers: booking parsing, my reservations filter, file util, auth command formatting

## Behaviors
* Admin day names accept short or full strings (MON or MONDAY)
* Client methods return `ERROR: not connected` if used before connect
* Tests reset some data files, manual data may be removed after test runs

## Phase 3

### GUI Panels
* **LoginPanel** - User login and account creation forms
* **MainMenuPanel** - Navigation hub with booking, reservations, pricing, and logout buttons
* **BookingPanel** - Search tables by date/time/party size, view seating chart, book tables, cancel by reservation ID
* **MyReservationsPanel** - View all user reservations with status, cancel selected reservation
* **AdminPanel** - Set hours, lock sections, upsert tables (admin-only access)
* **SeatingChartPanel** - Visual grid of available tables (green = available)

### Design Choices for GUI

**Architecture: CardLayout for Panel Navigation**
We chose CardLayout for the main GUI container because it allows seamless switching between panels (login, menu, booking, etc.) without creating new windows. This keeps the application in a single frame and maintains state between panel switches. It also allows for a simple way to hit all functionality requirements, simplicity is king. Reduces failure points and makes debugging easier.

**Thin Client Design**
The GUI client is intentionally "concise", it only handles user input and display. All business logic (validation, booking rules, availability checks) happens server side. This ensures consistency across multiple clients and prevents users from bypassing rules.

**Mock Objects for GUI Testing**
We created MockReservationClient and MockParentGUI to test panels in isolation without needing a running server. This allows unit testing of GUI logic (button clicks, field validation, status updates) independently from network code.

**Visual Seating Chart**
SeatingChartPanel uses JLabels in a GridLayout to display available tables. Each table is a green label showing the table ID and capacity. 

**Admin Access Control**
Rather than creating separate admin/user GUIs, we use a single GUI with conditional visibility. The admin button is hidden by default and only shown when the logged-in email equals "admin". This simplifies the codebase while maintaining security.

**Pre filled Form Defaults**
Date and time fields are pre-populated with example values (2025-12-15, 18:00) to show users the expected format and reduce input errors. The format hint is also shown in field labels.

**Exception Handling in Action Listeners**
All button action listeners wrap sendCommand() calls in try-catch blocks to prevent uncaught IOExceptions from crashing the GUI. Errors are displayed in status labels rather than dialog boxes for a less intrusive UX.

**Interface Driven Design**
Every panel and model class implements an interface. This supports the project requirement and enables dependency injection for testing (e.g., passing MockReservationClient instead of real client).

### New Features Added
* Account deletion via DELETE_USER command
* View Pricing button displays restaurant pricing info
* Seating chart updates dynamically when searching for tables
* Admin button only visible when logged in as admin user
* Pre-filled date/time fields with example format for easier input

### Interfaces and Tests
* Every GUI panel has a dedicated interface (LoginPanelInterface, BookingPanelInterface, etc.)
* Every model class has an interface (UserInterface, TableInterface, ReservationInterface, etc.)
* 100 JUnit tests covering all panels, models, and helpers

## Set Hours
* Admin selects a day and enters opening and closing times
* Sends command: `ADMIN_SET_HOURS DAY HH:mm HH:mm`
* Then displays success or error from server

## Lock Section
* Admin enters a date, time, and section name
* Sends command: `ADMIN_LOCK_SECTION yyyy-MM-dd HH:mm section`
* Shows the status label message based on server response

## Upsert Table
* Admin enters table ID, capacity, and section
* Table ID has to be numeric
* Sends command: `ADMIN_UPSERT_TABLE tableId capacity section`
* Status shows error if ID is not a number

## Testing
* Wrote AdminPanelTest with a fake client in such a way where there is no server needed
* Tested day dropdown contents
* Tested numeric validation for table ID
* Tested correct command strings
* Tested status label updates

## Manual Testing
* Ran GUI (Tried all admin actions)
* Checked valid and invalid inputs
* Verified correct protocol messages and replies

## Notes
* Only GUI logic was added (Backend logic - unchanged)
