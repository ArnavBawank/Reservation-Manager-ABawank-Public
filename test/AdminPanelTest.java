import org.junit.Test;
import javax.swing.*;
import static org.junit.Assert.*;

/**
 * AdminPanelTest.java
 * Tests for AdminPanel.
 * Only tests GUI logic.
 *
 * @author Arnav Bawankule
 * @version 1.0
 */
public class AdminPanelTest {

    /**
     * Fake client for testing.
     */
    private static class FakeClient extends ReservationClient {
        String lastCommand = "";
        String reply = "OK";

        public String sendCommand(String cmd) throws java.io.IOException {
            lastCommand = cmd;
            return reply;
        }
    }

    /**
     * Fake GUI for testing.
     */
    private static class FakeGUI extends ReservationGUI {
        FakeClient client = new FakeClient();

        public FakeGUI() {
            super("localhost", 8888, new MockReservationClient());
        }

        @Override
        public ReservationClient getClient() {
            return client;
        }
    }

    @Test
    public void testDayDropdownHas7Days() {
        FakeGUI gui = new FakeGUI();
        AdminPanel panel = new AdminPanel(gui);

        JComboBox<String> dayBox = panel.getDayDropdown();

        assertEquals(7, dayBox.getItemCount());
        assertEquals("MON", dayBox.getItemAt(0));
        assertEquals("SUN", dayBox.getItemAt(6));
    }

    @Test
    public void testTableIdMustBeNumeric() {
        FakeGUI gui = new FakeGUI();
        AdminPanel panel = new AdminPanel(gui);

        JTextField tableId = panel.getTableIdField();
        JButton upsertButton = panel.getUpsertTableButton();

        tableId.setText("ABC");  // invalid input
        upsertButton.doClick();

        JLabel status = panel.getStatusLabel();

        assertEquals("Table ID must be numeric", status.getText());
    }

    @Test
    public void testAdminSetHoursSendsCorrectCommand() {
        FakeGUI gui = new FakeGUI();
        AdminPanel panel = new AdminPanel(gui);

        JComboBox<String> dayBox = panel.getDayDropdown();
        JTextField openField = panel.getOpenField();
        JTextField closeField = panel.getCloseField();
        JButton setButton = panel.getSetHoursButton();
        JLabel status = panel.getStatusLabel();

        dayBox.setSelectedItem("MON");

        openField.setText("09:00");
        closeField.setText("17:00");

        gui.client.reply = "OK";
        setButton.doClick();

        assertEquals("ADMIN_SET_HOURS MON 09:00 17:00", gui.client.lastCommand);

        assertEquals("Hours updated successfully", status.getText());
    }
}