/**
 * reservation client interface
 *
 * defines client operations for sending protocol commands
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public interface ReservationClientInterface {
    void connect(String host, int port) throws java.io.IOException;
    void disconnect() throws java.io.IOException;
    String sendCommand(String command) throws java.io.IOException;
    String createAccount(String email, String password);
    String login(String email, String password);
    String logout();
    String listOpen(String formattedDateTime, int partySize);
    String book(String formattedDateTime, int partySize);
    String cancel(int reservationId);
    String listMyReservations();
    String deleteAccount();
}
