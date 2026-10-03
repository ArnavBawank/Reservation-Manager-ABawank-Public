import static org.junit.Assert.*;
import org.junit.Test;

/**
 * reservation client test
 *
 * checks error handling when not connected
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public class ReservationClientTest {

    @Test
    public void createAccountWithoutConnectReturnsError() {
        ReservationClient client = new ReservationClient();
        String res = client.createAccount("test@example.com", "pw");
        assertTrue(res.startsWith("ERROR"));
    }

    @Test
    public void loginWithoutConnectReturnsError() {
        ReservationClient client = new ReservationClient();
        String res = client.login("a@b.com", "123");
        assertTrue(res.startsWith("ERROR"));
    }

    @Test
    public void listOpenWithoutConnectReturnsError() {
        ReservationClient client = new ReservationClient();
        String res = client.listOpen("2025-12-01 18:00", 4);
        assertTrue(res.startsWith("ERROR"));
    }
}
