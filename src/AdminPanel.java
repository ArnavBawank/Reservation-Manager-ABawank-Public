import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AdminPanel.java
 * The admin screen for setting hours, locking tables, and upserting tables.
 *
 * @author Arnav Bawankule
 * @version 1.0
 */

public class AdminPanel extends JPanel implements AdminPanelInterface {
    private ReservationGUI parent;

    //Setting hours part
    private JComboBox<String> dayDropdown;
    private JTextField openField;
    private JTextField closeField;
    private JButton setHoursButton;

    //locking tables part
    private JTextField lockDateField;
    private JTextField lockTimeField;
    private JTextField lockSectionField;
    private JButton lockSectionButton;

    //upserting tables part
    private JTextField tableIdField;
    private JTextField sectionField;
    private JSpinner capacitySpinner;
    private JButton upsertTableButton;

    private JButton backButton;
    private JLabel statusLabel;

    public AdminPanel(ReservationGUI parent) {
        this.parent = parent;
        initComponents();
        layoutComponents();
        addListeners();
    }

    private void initComponents() {
        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        dayDropdown = new JComboBox<String>(days);
        openField = new JTextField(5);
        closeField = new JTextField(5);
        setHoursButton = new JButton("Set Hours");

        lockDateField = new JTextField(8);
        lockTimeField = new JTextField(5);
        lockSectionField = new JTextField(6);
        lockSectionButton = new JButton("Lock Section");

        tableIdField = new JTextField(5);
        capacitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        sectionField = new JTextField(6);
        upsertTableButton = new JButton("Add/Update Table");

        backButton = new JButton("Back");
        statusLabel = new JLabel(" "); 
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        JPanel hoursPanel = new JPanel();
        hoursPanel.setBorder(BorderFactory.createTitledBorder("Set Hours"));
        hoursPanel.add(new JLabel("Day:"));
        hoursPanel.add(dayDropdown);
        hoursPanel.add(new JLabel("Open:"));
        hoursPanel.add(openField);
        hoursPanel.add(new JLabel("Close:"));
        hoursPanel.add(closeField);
        hoursPanel.add(setHoursButton);

        JPanel lockPanel = new JPanel();
        lockPanel.setBorder(BorderFactory.createTitledBorder("Lock Section"));
        lockPanel.add(new JLabel("Date:"));
        lockPanel.add(lockDateField);
        lockPanel.add(new JLabel("Time:"));
        lockPanel.add(lockTimeField);
        lockPanel.add(new JLabel("Section:"));
        lockPanel.add(lockSectionField);
        lockPanel.add(lockSectionButton);

        JPanel upsertPanel = new JPanel();
        upsertPanel.setBorder(BorderFactory.createTitledBorder("Add/Update Table"));
        upsertPanel.add(new JLabel("Table ID:"));
        upsertPanel.add(tableIdField);
        upsertPanel.add(new JLabel("Capacity:"));
        upsertPanel.add(capacitySpinner);
        upsertPanel.add(new JLabel("Section:"));
        upsertPanel.add(sectionField);
        upsertPanel.add(upsertTableButton);

        JPanel navPanel = new JPanel();
        navPanel.add(backButton);

        mainPanel.add(hoursPanel);
        mainPanel.add(lockPanel);
        mainPanel.add(upsertPanel);
        mainPanel.add(navPanel);

        add(mainPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    //addListeners
    private void addListeners() {
        setHoursButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String day = (String) dayDropdown.getSelectedItem();
                String open = openField.getText().trim();
                String close = closeField.getText().trim();
                try {
                    String response = parent.getClient().sendCommand(
                        "ADMIN_SET_HOURS " + day + " " + open + " " + close
                    );
                    if (response.startsWith("OK")) {
                        setStatus("Hours updated successfully");
                    } else {
                        setStatus(response);
                    }
                } catch (Exception ex) {
                    setStatus("Error: " + ex.getMessage());
                }
            }
        });

        lockSectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String date = lockDateField.getText().trim();
                String time = lockTimeField.getText().trim();
                String section = lockSectionField.getText().trim();
                try {
                    String response = parent.getClient().sendCommand(
                        "ADMIN_LOCK_SECTION " + date + " " + time + " " + section
                    );
                    if (response.startsWith("OK")) {
                        setStatus("Section locked successfully");
                    } else {
                        setStatus(response);
                    }
                } catch (Exception ex) {
                    setStatus("Error: " + ex.getMessage());
                }
            }
        });

        upsertTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int tableId = Integer.parseInt(tableIdField.getText().trim());
                    int capacity = (int) capacitySpinner.getValue();
                    String section = sectionField.getText().trim();
                    String response = parent.getClient().sendCommand(
                        "ADMIN_UPSERT_TABLE " + tableId + " " + capacity + " " + section
                    );
                    if (response.startsWith("OK")) {
                        setStatus("Table added/updated successfully.");
                    } else {
                        setStatus(response);
                    }
                } catch (NumberFormatException ex) {
                    setStatus("Table ID must be numeric");
                } catch (Exception ex) {
                    setStatus("Error: " + ex.getMessage());
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showPanel("mainMenu");
            }
        });
    }

    @Override
    public void refresh() { }

    @Override
    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    // getter methods for testing
    public JComboBox<String> getDayDropdown() { return dayDropdown; }
    public JTextField getOpenField() { return openField; }
    public JTextField getCloseField() { return closeField; }
    public JButton getSetHoursButton() { return setHoursButton; }
    public JTextField getTableIdField() { return tableIdField; }
    public JButton getUpsertTableButton() { return upsertTableButton; }
    public JLabel getStatusLabel() { return statusLabel; }
}