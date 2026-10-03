import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * booking panel
 *
 * allows users to search for available tables, make bookings, and cancel reservations
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class BookingPanel extends JPanel implements BookingPanelInterface {
    private ReservationGUI parent;

    // search components
    private JTextField dateField;
    private JTextField timeField;
    private JSpinner partySizeSpinner;
    private JButton searchButton;

    // table list
    private JList<String> tableList;
    private DefaultListModel<String> tableListModel;

    // seating chart
    private SeatingChartPanel seatingChart;

    // booking components
    private JButton bookButton;

    // cancellation components
    private JTextField reservationIdField;
    private JButton cancelButton;

    // navigation
    private JButton backButton;
    private JLabel statusLabel;

    public BookingPanel(ReservationGUI parent) {
        this.parent = parent;
        initComponents();
        layoutComponents();
        addListeners();
    }

    private void initComponents() {
        dateField = new JTextField("2025-12-15", 10);
        timeField = new JTextField("18:00", 5);
        partySizeSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
        searchButton = new JButton("Search");

        tableListModel = new DefaultListModel<String>();
        tableList = new JList<String>(tableListModel);
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        seatingChart = new SeatingChartPanel();

        bookButton = new JButton("Book Selected Table");

        reservationIdField = new JTextField(10);
        cancelButton = new JButton("Cancel Reservation");

        backButton = new JButton("Back");
        statusLabel = new JLabel(" ");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // search panel at top - two rows
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Available Tables"));

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        inputRow.add(new JLabel("Date (yyyy-MM-dd):"));
        inputRow.add(dateField);
        inputRow.add(new JLabel("Time (HH:mm):"));
        inputRow.add(timeField);
        inputRow.add(new JLabel("Party Size:"));
        inputRow.add(partySizeSpinner);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(searchButton);

        searchPanel.add(inputRow, BorderLayout.CENTER);
        searchPanel.add(buttonRow, BorderLayout.SOUTH);

        // seating chart on left
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Seating Chart"));
        chartPanel.add(seatingChart, BorderLayout.CENTER);
        chartPanel.setPreferredSize(new Dimension(250, 200));

        // table list on right
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Available Tables"));
        listPanel.add(new JScrollPane(tableList), BorderLayout.CENTER);

        JPanel bookPanel = new JPanel();
        bookPanel.add(bookButton);
        listPanel.add(bookPanel, BorderLayout.SOUTH);

        // cancel panel
        JPanel cancelPanel = new JPanel();
        cancelPanel.setBorder(BorderFactory.createTitledBorder("Cancel Reservation"));
        cancelPanel.add(new JLabel("Reservation ID:"));
        cancelPanel.add(reservationIdField);
        cancelPanel.add(cancelButton);

        // combine chart and list side by side
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablesPanel.add(chartPanel);
        tablesPanel.add(listPanel);

        // combine tables and cancel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tablesPanel, BorderLayout.CENTER);
        centerPanel.add(cancelPanel, BorderLayout.SOUTH);

        // navigation panel at bottom
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.add(backButton, BorderLayout.WEST);
        navPanel.add(statusLabel, BorderLayout.CENTER);

        add(searchPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleBook();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showPanel("mainMenu");
            }
        });
    }

    private void handleSearch() {
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        int partySize = (int) partySizeSpinner.getValue();

        if (date.isEmpty() || time.isEmpty()) {
            setStatus("Please enter date and time");
            return;
        }

        String dateTime = date + " " + time;
        String response = parent.getClient().listOpen(dateTime, partySize);

        if (response.startsWith("OK")) {
            String[] tables = parseTableResponse(response);
            tableListModel.clear();
            if (tables.length == 0 || (tables.length == 1 && tables[0].isEmpty())) {
                setStatus("No tables available for this time");
                seatingChart.setAvailableTables(new String[0]);
            } else {
                for (String table : tables) {
                    if (!table.isEmpty()) {
                        tableListModel.addElement(table);
                    }
                }
                setStatus("Found " + tableListModel.size() + " available table(s)");
                seatingChart.setAvailableTables(tables);
            }
        } else {
            // friendlier error messages
            if (response.contains("cannot be parsed") || response.contains("parse")) {
                setStatus("Invalid format. Use date: yyyy-MM-dd, time: HH:mm");
            } else {
                setStatus(response);
            }
            seatingChart.setAvailableTables(new String[0]);
        }
    }

    private void handleBook() {
        if (!parent.isLoggedIn()) {
            setStatus("Please log in first");
            return;
        }

        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        int partySize = (int) partySizeSpinner.getValue();

        if (date.isEmpty() || time.isEmpty()) {
            setStatus("Please enter date and time");
            return;
        }

        String dateTime = date + " " + time;
        String response = parent.getClient().book(dateTime, partySize);

        if (response.startsWith("OK")) {
            // response format: OK reservationId tableId
            String[] parts = response.split(" ");
            if (parts.length >= 3) {
                setStatus("Booked! Reservation ID: " + parts[1] + " - Table: " + parts[2]);
            } else {
                setStatus("Booking successful");
            }
            clearTableList();
        } else {
            setStatus(response);
        }
    }

    private void handleCancel() {
        String idText = reservationIdField.getText().trim();
        if (idText.isEmpty()) {
            setStatus("Please enter a reservation ID");
            return;
        }

        try {
            int reservationId = Integer.parseInt(idText);
            String response = parent.getClient().cancel(reservationId);

            if (response.startsWith("OK")) {
                setStatus("Reservation cancelled successfully");
                reservationIdField.setText("");
            } else {
                setStatus(response);
            }
        } catch (NumberFormatException ex) {
            setStatus("Reservation ID must be numeric");
        }
    }

    @Override
    public void refresh() {
        // clear fields when panel is shown
        clearTableList();
        statusLabel.setText(" ");
    }

    @Override
    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    @Override
    public void clearTableList() {
        tableListModel.clear();
        seatingChart.setAvailableTables(new String[0]);
    }

    @Override
    public String[] parseTableResponse(String response) {
        // response format: OK tableId:capacity:section,tableId:capacity:section,...
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
                // format: Table T4 - Capacity: 4 - Section: Main
                result[i] = "Table " + parts[0] + " - Capacity: " + parts[1] + " - Section: " + parts[2];
            } else {
                result[i] = entries[i];
            }
        }
        return result;
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

    // getter methods for testing
    public JTextField getDateField() {
        return dateField;
    }

    public JTextField getTimeField() {
        return timeField;
    }

    public JSpinner getPartySizeSpinner() {
        return partySizeSpinner;
    }

    public JList<String> getTableList() {
        return tableList;
    }

    public DefaultListModel<String> getTableListModel() {
        return tableListModel;
    }

    public JTextField getReservationIdField() {
        return reservationIdField;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }
}
