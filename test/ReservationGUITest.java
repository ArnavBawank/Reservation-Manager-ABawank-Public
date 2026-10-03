import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;

/**
 * @author Daniel Gong (gong256)
 * @version 2025-12-03
 * Unit tests for the ReservationGUI class.
 */
public class ReservationGUITest {

    private ReservationGUI gui;

    @Before
    public void setup() {
        gui = new ReservationGUI("localhost", 8888, new MockReservationClient());
    }

    @Test
    public void testConstructorInitializesFields() {
        assertNotNull(gui.getClient());
        assertNull(gui.getCurrentEmail());
        assertFalse(gui.isLoggedIn());

        // card layout and panels exist
        assertNotNull(gui.cardLayout);
        assertNotNull(gui.cardPanel);
    }

    @Test
    public void testSetLoggedIn() {
        gui.setLoggedIn(12345, "test@example.com");

        assertTrue(gui.isLoggedIn());
        assertEquals("test@example.com", gui.getCurrentEmail());
        assertEquals(12345, gui.currentSessionId);
    }

    @Test
    public void testLogoutStateChange() {
        gui.setLoggedIn(12345, "test@example.com");
        gui.logout();

        assertFalse(gui.isLoggedIn());
        assertNull(gui.getCurrentEmail());
        assertEquals(-1, gui.currentSessionId);
    }

    @Test
    public void testShowPanelValidName() {
        gui.showPanel("login");
        // no exception thrown means success
    }
}
