import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;

/**
 * @author Daniel Gong (gong256)
 * @version 2025-12-03
 * Test class for the MainMenuPanel component.
 */
public class MainMenuPanelTest {

    private MainMenuPanel panel;
    private MockParentGUI mockGui;

    /**
     * Sets up a fresh test environment before each test.
     * Initializes the mock GUI and a new instance of MainMenuPanel.
     */
    @Before
    public void setup() {
        mockGui = new MockParentGUI(new MockReservationClient());
        panel = new MainMenuPanel(mockGui);
    }

    /**
     * Tests that the refresh method correctly updates
     * the welcome label to include the current user's email.
     */
    @Test
    public void testRefreshUpdatesWelcomeText() {
        mockGui.currentEmail = "hello@test.com";
        panel.refresh();

        assertEquals("Welcome, hello@test.com", panel.welcomeLabel.getText());
    }

    /**
     * Tests that the navigation buttons call the parent's showPanel method.
     */
    @Test
    public void testNavigationButtonsCallParent() {
        // Simulate clicking "Find & Book Tables"
        panel.bookingButton.doClick();
        assertEquals("booking", mockGui.lastShownPanel);

        // Simulate clicking "My Reservations"
        panel.myReservationsButton.doClick();
        assertEquals("myReservations", mockGui.lastShownPanel);
    }

    /**
     * Tests that clicking the logout button calls the parent's logout method.
     */
    @Test
    public void testLogoutCallsParentLogout() {
        panel.logoutButton.doClick();

        assertTrue(mockGui.logoutCalled);
    }
}
