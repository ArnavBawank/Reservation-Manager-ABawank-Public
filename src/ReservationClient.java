import java.io.*;
import java.net.*;

/**
 * Handles all network communication with the reservation server.
 * <p>
 * This class manages the connection (Socket) and sends specific commands
 * like "LOGIN" or "BOOK" to the server, then returns the server's response.
 * </p>
 * @author Daniel Gong (gong256)
 * @version 2025-11-10
 */
public class ReservationClient implements ReservationClientInterface {
    private Socket socket;
    private BufferedReader bfr;
    private PrintWriter pw;

    /**
     * Connects to the server at the specified address and port.
     * <p>
     * This must be called before sending any commands.
     * </p>
     * @param host the server address (e.g., "localhost")
     * @param port the port number the server is listening on
     * @throws IOException if the connection fails (e.g., server is offline)
     */
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Closes the connection to the server and cleans up resources.
     * @throws IOException if an error occurs while closing the connection
     */
    public void disconnect() throws IOException {
        if (pw != null) pw.close();
        if (bfr != null) bfr.close();
        if (socket != null) socket.close();
    }

    /**
     * Sends a raw text command to the server and waits for a reply.
     * @param command the text string to send
     * @return the single-line response received from the server
     * @throws IOException if the network connection is broken
     */
    public String sendCommand(String command) throws IOException {
        if (pw == null || bfr == null) {
            return "ERROR: not connected";
        }
        pw.println(command);
        return bfr.readLine();
    }

    /**
     * Tells the server to create a new user account.
     * @param email    the email for the new account
     * @param password the password for the new account
     * @return the server's response, or an error message starting with "ERROR" if the network fails
     */
    public String createAccount(String email, String password) {
        try {
            return sendCommand("CREATE_USER " + email + " " + password);
        } catch (IOException e) {
            return "ERROR: client IO exception: " + e.getMessage();
        }
    }

    /**
     * Attempts to log the user in.
     * @param email    the user's email
     * @param password the user's password
     * @return the server's response (e.g., success message), or an error string
     */
    public String login(String email, String password) {
        try {
            return sendCommand("LOGIN " + email + " " + password);
        } catch (IOException e) {
            return "ERROR: client IO exception: " + e.getMessage();
        }
    }

    /**
     * Tells the server to end the current session.
     * @return the server's confirmation message, or an error string
     */
    public String logout() {
        try {
            return sendCommand("LOGOUT");
        } catch (IOException e) {
            return "ERROR: client IO exception: " + e.getMessage();
        }
    }

    /**
     * Asks the server for a list of available tables.
     * @param formattedDateTime the date and time to check (e.g., "2025-11-10 18:00")
     * @param partySize         the number of people in the group
     * @return a string listing available slots, or an error string
     */
    public String listOpen(String formattedDateTime, int partySize) {
        try {
            return sendCommand("LIST_OPEN " + formattedDateTime + " " + partySize);
        } catch (IOException e) {
            return "ERROR: client IO exception: " + e.getMessage();
        }
    }

    /**
     * Requests to book a reservation.
     * @param formattedDateTime the date and time to book
     * @param partySize         the number of people in the group
     * @return the server's confirmation or denial message, or an error string
     */
    public String book(String formattedDateTime, int partySize) {
        try {
            return sendCommand("BOOK " + formattedDateTime + " " + partySize);
        } catch (IOException e) {
            return "ERROR: client IO exception: " + e.getMessage();
        }
    }

    /**
     * Tells the server to cancel a specific reservation.
     * @param reservationId the unique ID of the reservation to cancel
     * @return the server's response, or an error string
     */
    public String cancel(int reservationId) {
        try {
            return sendCommand("CANCEL " + reservationId);
        } catch (IOException e) {
            return "ERROR: client IO exception: " + e.getMessage();
        }
    }

    /**
     * Asks the server for a history of reservations made by the current user.
     * @return a string listing the user's reservations, or an error string
     */
    public String listMyReservations() {
        try {
            return sendCommand("LIST_MY_RESERVATIONS");
        } catch (IOException e) {
            return "ERROR: client IO exception: " + e.getMessage();
        }
    }

    /**
     * Deletes the current user's account from the server.
     * @return the server's response, or an error string
     */
    public String deleteAccount() {
        try {
            return sendCommand("DELETE_USER");
        } catch (IOException e) {
            return "ERROR: client IO exception: " + e.getMessage();
        }
    }
}
