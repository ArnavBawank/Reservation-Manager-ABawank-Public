import javax.swing.*;
import java.awt.*;
/**
* @author Daniel Gong (gong256)
* @version 2025-12-03
* The main GUI class for the Reservation System application.
*/
public class ReservationGUI extends JFrame implements ReservationGUIInterface {

    private ReservationClient client;
    public CardLayout cardLayout;
    public JPanel cardPanel;

    private LoginPanel loginPanel;
    private MainMenuPanel mainMenuPanel;
    private BookingPanel bookingPanel;
    private MyReservationsPanel myReservationsPanel;
    private AdminPanel adminPanel;

    public int currentSessionId = -1;
    public String currentEmail = null;
    private boolean loggedIn = false;

    /**
     * Default constructor that initializes the GUI and prompts the user
     * for server host and port.
     * <p>
     * Attempts to connect to the server using {@link ReservationClient}.
     * If the connection fails, shows an error message and exits.
     * </p>
     */
    public ReservationGUI() {
        super("Reservation System");

        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Prompt for host/port
        String host = JOptionPane.showInputDialog(this, "Server host:", "localhost");
        if (host == null || host.isEmpty()) host = "localhost";

        String portStr = JOptionPane.showInputDialog(this, "Server port:", "8888");
        int port = 8888;
        try {
            port = Integer.parseInt(portStr);
        } catch (Exception ignored) {
            // use default port
        }

        client = new ReservationClient();
        try {
            client.connect(host, port);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Failed to connect to server: " + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create panels
        loginPanel = new LoginPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        bookingPanel = new BookingPanel(this);
        myReservationsPanel = new MyReservationsPanel(this);
        adminPanel = new AdminPanel(this);

        cardPanel.add(loginPanel, "login");
        cardPanel.add(mainMenuPanel, "mainMenu");
        cardPanel.add(bookingPanel, "booking");
        cardPanel.add(myReservationsPanel, "myReservations");
        cardPanel.add(adminPanel, "admin");

        add(cardPanel);

        showPanel("login");
    }

    /**
     * Test-friendly constructor that accepts an already-configured client.
     * Does not attempt to connect; useful for unit tests with mock clients.
     */
    public ReservationGUI(String host, int port, ReservationClient client) {
        super("Reservation System");

        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.client = client;

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create panels
        loginPanel = new LoginPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        bookingPanel = new BookingPanel(this);
        myReservationsPanel = new MyReservationsPanel(this);
        adminPanel = new AdminPanel(this);

        cardPanel.add(loginPanel, "login");
        cardPanel.add(mainMenuPanel, "mainMenu");
        cardPanel.add(bookingPanel, "booking");
        cardPanel.add(myReservationsPanel, "myReservations");
        cardPanel.add(adminPanel, "admin");

        add(cardPanel);

        showPanel("login");
    }

    /**
     * Switches the visible panel in the GUI.
     *
     * @param name the name of the panel to display
     */
    public void showPanel(String name) {
        cardLayout.show(cardPanel, name);
    }

    /**
     * Marks the user as logged in and stores session information.
     * <p>
     * Also refreshes the main menu panel to display the user's email.
     * </p>
     *
     * @param sessionId the session ID returned by the server
     * @param email     the email of the logged-in user
     */
    public void setLoggedIn(int sessionId, String email) {
        this.currentSessionId = sessionId;
        this.currentEmail = email;
        this.loggedIn = true;
        mainMenuPanel.refresh();
    }

    /**
     * Logs out the current user, clearing session information and
     * returning to the login panel.
     */
    public void logout() {
        this.currentSessionId = -1;
        this.currentEmail = null;
        this.loggedIn = false;
        showPanel("login");
    }

    /**
     * Returns the client used to communicate with the server.
     *
     * @return the {@link ReservationClient} instance
     */
    public ReservationClient getClient() {
        return client;
    }

    /**
     * Returns the email of the currently logged-in user.
     *
     * @return the current user's email, or null if not logged in
     */
    public String getCurrentEmail() {
        return currentEmail;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
}

