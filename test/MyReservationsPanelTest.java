import org.junit.Test;
import org.junit.Before;
import javax.swing.*;
import static org.junit.Assert.*;

/**
 * my reservations panel test
 *
 * tests for my reservations panel gui logic
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class MyReservationsPanelTest {

    private MockReservationClient mockClient;
    private MockParentGUI mockGui;
    private MyReservationsPanel panel;

    @Before
    public void setUp() {
        mockClient = new MockReservationClient();
        mockGui = new MockParentGUI(mockClient);
        panel = new MyReservationsPanel(mockGui);
    }

    @Test
    public void testParseReservationResponseValid() {
        String response = "OK 12345:T1:BOOKED,67890:T2:CANCELLED";
        String[] result = panel.parseReservationResponse(response);

        assertEquals(2, result.length);
        assertEquals("ID: 12345 - Table: T1 - Status: BOOKED", result[0]);
        assertEquals("ID: 67890 - Table: T2 - Status: CANCELLED", result[1]);
    }

    @Test
    public void testParseReservationResponseEmpty() {
        String response = "OK ";
        String[] result = panel.parseReservationResponse(response);

        assertEquals(0, result.length);
    }

    @Test
    public void testParseReservationResponseSingle() {
        String response = "OK 99999:VIP1:BOOKED";
        String[] result = panel.parseReservationResponse(response);

        assertEquals(1, result.length);
        assertEquals("ID: 99999 - Table: VIP1 - Status: BOOKED", result[0]);
    }

    @Test
    public void testParseReservationResponseError() {
        String response = "ERROR: not logged in";
        String[] result = panel.parseReservationResponse(response);

        assertEquals(0, result.length);
    }

    @Test
    public void testParseReservationResponseNull() {
        String[] result = panel.parseReservationResponse(null);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSelectedReservationIdNoSelection() {
        String result = panel.getSelectedReservationId();
        assertNull("Should return null when nothing selected", result);
    }

    @Test
    public void testGetSelectedReservationIdWithSelection() {
        panel.getReservationListModel().addElement("ID: 12345 - Table: T1 - Status: BOOKED");
        panel.getReservationList().setSelectedIndex(0);

        String result = panel.getSelectedReservationId();
        assertEquals("12345", result);
    }

    @Test
    public void testSetStatus() {
        panel.setStatus("Test status message");
        assertEquals("Test status message", panel.getStatusLabel().getText());
    }

    @Test
    public void testReservationListExists() {
        JList<String> list = panel.getReservationList();
        assertNotNull("Reservation list should exist", list);
    }

    @Test
    public void testReservationListSingleSelection() {
        JList<String> list = panel.getReservationList();
        assertEquals(ListSelectionModel.SINGLE_SELECTION, list.getSelectionMode());
    }

    @Test
    public void testEmptyListHandling() {
        mockClient.listMyReservationsResponse = "OK ";
        panel.refresh();

        assertEquals(0, panel.getReservationListModel().size());
        assertEquals("No reservations found", panel.getStatusLabel().getText());
    }

    @Test
    public void testRefreshPopulatesList() {
        mockClient.listMyReservationsResponse = "OK 11111:T1:BOOKED,22222:T2:BOOKED";
        panel.refresh();

        assertEquals(2, panel.getReservationListModel().size());
    }

    @Test
    public void testRefreshWithMultipleReservations() {
        mockClient.listMyReservationsResponse = "OK 11111:T1:BOOKED,22222:T2:CANCELLED,33333:T3:BOOKED";
        panel.refresh();

        assertEquals(3, panel.getReservationListModel().size());
        assertTrue(panel.getStatusLabel().getText().contains("3"));
    }
}
