import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
* @author Daniel Gong (gong256)
* @version 2025-12-03
* This panel serves as the main menu for the reservation system,
* allowing users to navigate to booking, view reservations, access
*/
public class MainMenuPanel extends JPanel implements MainMenuPanelInterface {
    private ReservationGUI parent;
    public JLabel welcomeLabel;
    public JButton bookingButton;
    public JButton myReservationsButton;
    public JButton pricingButton;
    public JButton adminButton;
    public JButton logoutButton;

    /**
     * Constructs a new MainMenuPanel attached to the given parent GUI.
     *
     * @param parent the {@link ReservationGUI} that owns this panel
     */
    public MainMenuPanel(ReservationGUI parent) {
        this.parent = parent;

        setLayout(new GridLayout(6, 1));

        welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);

        bookingButton = new JButton("Find & Book Tables");
        myReservationsButton = new JButton("My Reservations");
        pricingButton = new JButton("View Pricing");
        adminButton = new JButton("Admin Panel");
        logoutButton = new JButton("Logout");

        // hide admin button by default - shown only for admin user
        adminButton.setVisible(false);

        bookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showPanel("booking");
            }
        });
        myReservationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showPanel("myReservations");
            }
        });
        pricingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPricing();
            }
        });
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showPanel("admin");
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.getClient().logout();
                parent.logout();
            }
        });

        add(welcomeLabel);
        add(bookingButton);
        add(myReservationsButton);
        add(pricingButton);
        add(adminButton);
        add(logoutButton);
    }

    private void showPricing() {
        String pricing = "=== Restaurant Pricing ===\n\n" +
                         "Standard Seating: $25 per person\n" +
                         "Patio Seating: $30 per person\n" +
                         "Private Room: $100 flat rate + $25 per person\n\n" +
                         "Reservations are free to make and cancel.";
        JOptionPane.showMessageDialog(this, pricing, "Pricing Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates the welcome label and shows admin button if user is admin.
     */
    public void refresh() {
        String email = parent.getCurrentEmail();
        welcomeLabel.setText("Welcome, " + email);

        // show admin button only for admin user
        boolean isAdmin = "admin".equals(email);
        adminButton.setVisible(isAdmin);
    }
}

