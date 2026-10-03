import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

/**
 * client handler
 *
 * parses commands from one socket and invokes services
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public class ClientHandler implements ClientHandlerInterface {

    private final Socket socket;
    private final AuthServiceSimple auth;
    private final BookingService booking;
    private final AvailabilityService availability;
    private final AdminService admin;
    private final Rules rules;
    private final ReservationStore reservations;
    private final SeatingStore seating;
    private final ConfigStore config;
    private final SessionService sessions;
    private final MyReservationsService myReservations;

    private BufferedReader in;
    private PrintWriter out;
    private int currentSessionId = -1;
    private int currentUserId = -1;

    public ClientHandler(Socket socket,
                         AuthServiceSimple auth,
                         BookingService booking,
                         AvailabilityService availability,
                         AdminService admin,
                         Rules rules,
                         ReservationStore reservations,
                         SeatingStore seating,
                         ConfigStore config,
                         SessionService sessions) {
        this.socket = socket;
        this.auth = auth;
        this.booking = booking;
        this.availability = availability;
        this.admin = admin;
        this.rules = rules;
        this.reservations = reservations;
        this.seating = seating;
        this.config = config;
        this.sessions = sessions;
        this.myReservations = new MyReservationsService();
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = AuthCommandHelper.splitCommand(line);
                if (parts.length == 0) {
                    sendError("empty command");
                    continue;
                }
                String cmd = parts[0].toUpperCase(Locale.ROOT);
                try {
                    switch (cmd) {
                        case "CREATE_USER":
                            handleCreateUser(parts);
                            break;
                        case "LOGIN":
                            handleLogin(parts);
                            break;
                        case "LOGOUT":
                            handleLogout();
                            break;
                        case "LIST_OPEN":
                            handleListOpen(parts);
                            break;
                        case "BOOK":
                            handleBook(parts);
                            break;
                        case "CANCEL":
                            handleCancel(parts);
                            break;
                        case "LIST_MY_RESERVATIONS":
                            handleListMine();
                            break;
                        case "ADMIN_SET_HOURS":
                            handleAdminSetHours(parts);
                            break;
                        case "ADMIN_LOCK_SECTION":
                            handleAdminLock(parts);
                            break;
                        case "ADMIN_UPSERT_TABLE":
                            handleAdminUpsert(parts);
                            break;
                        case "DELETE_USER":
                            handleDeleteUser();
                            break;
                        default:
                            sendError("unknown command");
                    }
                } catch (Exception e) {
                    sendError(e.getMessage() == null ? "internal error" : e.getMessage());
                }
            }
        } catch (IOException e) {
            // connection dropped, ignore
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    private void handleCreateUser(String[] parts) {
        if (parts.length < 3) {
            sendError("need email and password");
            return;
        }
        int id = auth.createUser(parts[1], parts[2]);
        if (id <= 0) {
            sendError("could not create user");
        } else {
            sendOk(String.valueOf(id));
        }
    }

    private void handleLogin(String[] parts) {
        if (parts.length < 3) {
            sendError("need email and password");
            return;
        }
        int sid = auth.login(parts[1], parts[2]);
        if (sid <= 0) {
            sendError("login failed");
            return;
        }
        currentSessionId = sid;
        String uidStr = sessions.getUserId(String.valueOf(sid));
        try {
            currentUserId = uidStr == null ? -1 : Integer.parseInt(uidStr);
        } catch (NumberFormatException e) {
            currentUserId = -1;
        }
        sendOk(String.valueOf(sid));
    }

    private void handleLogout() {
        if (currentSessionId != -1) {
            sessions.removeSession(String.valueOf(currentSessionId));
        }
        currentSessionId = -1;
        currentUserId = -1;
        sendOk("bye");
    }

    private void handleListOpen(String[] parts) {
        if (parts.length < 4) {
            sendError("need date time and party size");
            return;
        }
        TimeSlot slot = BookingCommandHelper.parseSlot(parts[1], parts[2]);
        int size = BookingCommandHelper.parsePartySize(parts[3]);
        List<Table> open = availability.listOpenForParty(slot, size);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < open.size(); i++) {
            Table t = open.get(i);
            if (i > 0) sb.append(",");
            sb.append(t.getId()).append(":").append(t.getCapacity()).append(":").append(t.getSection());
        }
        sendOk(sb.toString());
    }

    private void handleBook(String[] parts) throws ValidationException, NoAvailabilityException {
        if (!ensureLoggedIn()) return;
        if (parts.length < 4) {
            sendError("need date time and party size");
            return;
        }
        TimeSlot slot = BookingCommandHelper.parseSlot(parts[1], parts[2]);
        int size = BookingCommandHelper.parsePartySize(parts[3]);
        List<Table> open = availability.listOpenForParty(slot, size);
        if (open.isEmpty()) {
            throw new NoAvailabilityException("no table available");
        }
        Table pick = open.get(0);
        rules.validate(String.valueOf(currentUserId), slot, size, pick);
        Reservation r = booking.bookBestFit(String.valueOf(currentUserId), size, slot);
        sendOk(r.getId() + " " + r.getTableId());
    }

    private void handleCancel(String[] parts) {
        if (!ensureLoggedIn()) return;
        if (parts.length < 2) {
            sendError("need reservation id");
            return;
        }
        int ridInt;
        try {
            ridInt = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            sendError("reservation id must be numeric");
            return;
        }
        String rid = String.valueOf(ridInt);
        boolean ok = booking.cancel(rid);
        if (ok) {
            sendOk("cancelled");
        } else {
            sendError("not found");
        }
    }

    private void handleListMine() {
        if (!ensureLoggedIn()) return;
        List<Reservation> mine = myReservations.listForUser(String.valueOf(currentUserId));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mine.size(); i++) {
            Reservation r = mine.get(i);
            if (i > 0) sb.append(",");
            sb.append(r.getId()).append(":").append(r.getTableId()).append(":").append(r.getStatus());
        }
        sendOk(sb.toString());
    }

    private void handleAdminSetHours(String[] parts) {
        if (parts.length < 4) {
            sendError("need day open close");
            return;
        }
        String dayRaw = parts[1].toUpperCase(Locale.ROOT);
        DayOfWeek day;
        try {
            day = DayOfWeek.valueOf(dayRaw);
        } catch (IllegalArgumentException ex) {
            day = parseDayShort(dayRaw);
        }
        LocalTime open = LocalTime.parse(parts[2]);
        LocalTime close = LocalTime.parse(parts[3]);
        admin.setHours(day, open, close);
        sendOk("set");
    }

    private void handleAdminLock(String[] parts) {
        if (parts.length < 4) {
            sendError("need date time section");
            return;
        }
        TimeSlot slot = BookingCommandHelper.parseSlot(parts[1], parts[2]);
        String section = parts[3];
        admin.lockSection(slot, section);
        sendOk("locked");
    }

    private void handleAdminUpsert(String[] parts) {
        if (parts.length < 4) {
            sendError("need table id capacity section");
            return;
        }
        int id;
        int cap;
        try {
            id = Integer.parseInt(parts[1]);
            cap = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            sendError("table id and capacity must be numeric");
            return;
        }
        String sec = parts[3];
        admin.upsertTable(id, cap, sec);
        sendOk("ok");
    }

    private boolean ensureLoggedIn() {
        if (currentUserId == -1) {
            sendError("not logged in");
            return false;
        }
        return true;
    }

    private void sendOk(String payload) {
        out.println("OK " + payload);
    }

    private void sendError(String msg) {
        out.println("ERROR: " + msg);
    }

    private void handleDeleteUser() {
        if (!ensureLoggedIn()) return;
        auth.deleteUser(String.valueOf(currentUserId));
        currentSessionId = -1;
        currentUserId = -1;
        sendOk("deleted");
    }

    private DayOfWeek parseDayShort(String s) {
        switch (s) {
            case "MON": return DayOfWeek.MONDAY;
            case "TUE": return DayOfWeek.TUESDAY;
            case "WED": return DayOfWeek.WEDNESDAY;
            case "THU": return DayOfWeek.THURSDAY;
            case "FRI": return DayOfWeek.FRIDAY;
            case "SAT": return DayOfWeek.SATURDAY;
            case "SUN": return DayOfWeek.SUNDAY;
            default: throw new IllegalArgumentException("invalid day");
        }
    }
}
