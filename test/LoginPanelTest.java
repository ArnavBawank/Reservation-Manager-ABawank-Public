import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;

/**
 * @author Daniel Gong (gong256)
 * @version 2025-12-03
 * This test suite verifies core behaviors of the login panel, including
 * clearing input fields and updating status messages.
 */
public class LoginPanelTest {

    private LoginPanel panel;
    private MockParentGUI mockGui;
    private MockReservationClient mockClient;

    /**
     * Sets up a fresh test environment before each test.
     */
    @Before
    public void setup() {
        mockClient = new MockReservationClient();
        mockGui = new MockParentGUI(mockClient);
        panel = new LoginPanel(mockGui);
    }

    /**
     * Tests that calling clearFields properly resets
     * the email and password input fields to empty values.
     */
    @Test
    public void testFieldClearing() {
        panel.emailField.setText("abc@example.com");
        panel.passwordField.setText("password");

        panel.clearFields();

        assertEquals("", panel.emailField.getText());
        assertEquals("", new String(panel.passwordField.getPassword()));
    }

    /**
     * Tests that status messages are correctly updated.
     */
    @Test
    public void testStatusMessageUpdates() {
        panel.setStatusMessage("Login failed");
        assertEquals("Login failed", panel.statusLabel.getText());

        panel.setStatusMessage("Success");
        assertEquals("Success", panel.statusLabel.getText());
    }
}
