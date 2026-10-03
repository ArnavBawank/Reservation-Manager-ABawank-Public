import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.File;

/**
 * reservation server test
 *
 * ensures server can start and stop without throwing
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public class ReservationServerTest {

    @After
    public void clean() {
        new File("data/server_test_users.txt").delete();
        new File("data/server_test_seating.txt").delete();
        new File("data/reservations.txt").delete();
        new File("data/config.txt").delete();
    }

    @Test
    public void startAndStopWorks() {
        SessionService sessions = new SessionMap();
        FileUserStore users = new FileUserStore("data/server_test_users.txt");
        FileSeatingStore seating = new FileSeatingStore("data/server_test_seating.txt");
        FileReservationStore res = new FileReservationStore();
        AvailabilityService avail = new AvailabilityServiceSimple(seating, res);
        BookingService booking = new BookingServiceSimple(avail, res);
        FileConfigStore cfg = new FileConfigStore(new FileUtil());
        AdminService admin = new AdminServiceSimple(seating, cfg);
        Rules rules = new RulesSimple(cfg);

        ReservationServer server = new ReservationServer(
                0,
                new AuthServiceSimple(users, sessions),
                booking,
                avail,
                admin,
                rules,
                res,
                seating,
                cfg,
                sessions);
        server.start();
        server.stop();
        assertTrue(true);
    }
}
