import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * my reservations panel
 *
 * displays user reservations and allows cancellation
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class MyReservationsPanel extends JPanel implements MyReservationsPanelInterface {
    private ReservationGUI parent;

    // reservation list components
    private JList<String> reservationList;
    private DefaultListModel<String> reservationListModel;

    // buttons
    private JButton refreshButton;
    private JButton cancelSelectedButton;
    private JButton backButton;

    // status
    private JLabel statusLabel;

    public MyReservationsPanel(ReservationGUI parent) {
        this.parent = parent;
        initComponents();
        layoutComponents();
        addListeners();
    }

    private void initComponents() {
        reservationListModel = new DefaultListModel<String>();
        reservationList = new JList<String>(reservationListModel);
        reservationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshButton = new JButton("Refresh");
        cancelSelectedButton = new JButton("Cancel Selected");
        backButton = new JButton("Back");

        statusLabel = new JLabel(" ");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // title panel
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("My Reservations"));

        // list panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Your Reservations"));
        listPanel.add(new JScrollPane(reservationList), BorderLayout.CENTER);

        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelSelectedButton);

        // navigation panel
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.add(backButton, BorderLayout.WEST);
        navPanel.add(statusLabel, BorderLayout.CENTER);

        // combine buttons with list
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(listPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        cancelSelectedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCancelSelected();
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showPanel("mainMenu");
            }
        });
    }

    @Override
    public void refresh() {
        String response = parent.getClient().listMyReservations();

        if (response.startsWith("OK")) {
            String[] reservations = parseReservationResponse(response);
            reservationListModel.clear();

            if (reservations.length == 0 || (reservations.length == 1 && reservations[0].isEmpty())) {
                setStatus("No reservations found");
            } else {
                for (String reservation : reservations) {
                    if (!reservation.isEmpty()) {
                        reservationListModel.addElement(reservation);
                    }
                }
                setStatus("Found " + reservationListModel.size() + " reservation(s)");
            }
        } else {
            setStatus(response);
        }
    }

    private void handleCancelSelected() {
        String selectedId = getSelectedReservationId();
        if (selectedId == null) {
            setStatus("Please select a reservation to cancel");
            return;
        }

        try {
            int reservationId = Integer.parseInt(selectedId);
            String response = parent.getClient().cancel(reservationId);

            if (response.startsWith("OK")) {
                setStatus("Reservation cancelled successfully");
                refresh();
            } else {
                setStatus(response);
            }
        } catch (NumberFormatException ex) {
            setStatus("Invalid reservation ID");
        }
    }

    @Override
    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    @Override
    public String[] parseReservationResponse(String response) {
        // response format: OK id:tableId:STATUS,id:tableId:STATUS,...
        if (response == null || !response.startsWith("OK")) {
            return new String[0];
        }

        String payload = response.substring(3).trim();
        if (payload.isEmpty()) {
            return new String[0];
        }

        String[] entries = payload.split(",");
        String[] result = new String[entries.length];

        for (int i = 0; i < entries.length; i++) {
            String[] parts = entries[i].split(":");
            if (parts.length >= 3) {
                // format: ID: 12345 - Table: T4 - Status: BOOKED
                result[i] = "ID: " + parts[0] + " - Table: " + parts[1] + " - Status: " + parts[2];
            } else {
                result[i] = entries[i];
            }
        }
        return result;
    }

    @Override
    public String getSelectedReservationId() {
        String selected = reservationList.getSelectedValue();
        if (selected == null) {
            return null;
        }

        // extract id from format: ID: 12345 - Table: T4 - Status: BOOKED
        if (selected.startsWith("ID: ")) {
            int dashIndex = selected.indexOf(" - ");
            if (dashIndex > 4) {
                return selected.substring(4, dashIndex);
            }
        }
        return null;
    }

    // getter methods for testing
    public JList<String> getReservationList() {
        return reservationList;
    }

    public DefaultListModel<String> getReservationListModel() {
        return reservationListModel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }
}
