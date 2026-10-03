import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * reservation server implementation
 *
 * opens a socket, accepts clients, and hands each client to a handler thread
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public class ReservationServer implements ServerInterface, Runnable {

    private final int port;
    private final AuthServiceSimple auth;
    private final BookingService booking;
    private final AvailabilityService availability;
    private final AdminService admin;
    private final Rules rules;
    private final ReservationStore reservations;
    private final SeatingStore seating;
    private final ConfigStore config;
    private final SessionService sessions;

    private volatile boolean running = false;
    private Thread acceptThread;
    private ServerSocket serverSocket;

    public ReservationServer(int port,
                             AuthServiceSimple auth,
                             BookingService booking,
                             AvailabilityService availability,
                             AdminService admin,
                             Rules rules,
                             ReservationStore reservations,
                             SeatingStore seating,
                             ConfigStore config,
                             SessionService sessions) {
        this.port = port;
        this.auth = auth;
        this.booking = booking;
        this.availability = availability;
        this.admin = admin;
        this.rules = rules;
        this.reservations = reservations;
        this.seating = seating;
        this.config = config;
        this.sessions = sessions;
    }

    // convenience constructor that wires defaults
    public ReservationServer() {
        this.port = 8888;
        SessionService sess = new SessionMap();
        FileUserStore us = new FileUserStore("data/users.txt");
        this.auth = new AuthServiceSimple(us, sess);

        FileSeatingStore ss = new FileSeatingStore("data/seating.txt");
        FileReservationStore rs = new FileReservationStore();
        this.reservations = rs;
        this.seating = ss;
        this.availability = new AvailabilityServiceSimple(ss, rs);
        this.booking = new BookingServiceSimple(availability, rs);

        FileUtil util = new FileUtil();
        FileConfigStore cfg = new FileConfigStore(util);
        this.config = cfg;
        this.admin = new AdminServiceSimple(ss, cfg);
        this.rules = new RulesSimple(cfg);
        this.sessions = sess;
    }

    @Override
    public void start() {
        if (running) return;
        running = true;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            running = false;
            throw new RuntimeException("failed to start server: " + e.getMessage(), e);
        }
        acceptThread = new Thread(this);
        acceptThread.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket s = serverSocket.accept();
                ClientHandler h = new ClientHandler(
                        s,
                        auth,
                        booking,
                        availability,
                        admin,
                        rules,
                        reservations,
                        seating,
                        config,
                        sessions);
                new Thread(h).start();
            } catch (IOException e) {
                if (running) {
                    System.out.println("accept loop error: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void stop() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            // ignore shutdown errors
        }
        if (acceptThread != null) {
            acceptThread.interrupt();
        }
    }

    public static void main(String[] args) {
        ReservationServer srv = new ReservationServer();
        srv.start();
        System.out.println("server running on port 8888");
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            srv.stop();
        }
    }
}
