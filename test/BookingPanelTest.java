import org.junit.Test;
import org.junit.Before;
import javax.swing.*;
import static org.junit.Assert.*;

/**
 * booking panel test
 *
 * tests for booking panel gui logic
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class BookingPanelTest {

    private MockReservationClient mockClient;
    private MockParentGUI mockGui;
    private BookingPanel panel;

    @Before
    public void setUp() {
        mockClient = new MockReservationClient();
        mockGui = new MockParentGUI(mockClient);
        panel = new BookingPanel(mockGui);
    }

    @Test
    public void testPartySizeSpinnerBounds() {
        JSpinner spinner = panel.getPartySizeSpinner();
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();

        assertEquals("Minimum party size should be 1", 1, model.getMinimum());
        assertEquals("Maximum party size should be 20", 20, model.getMaximum());
        assertEquals("Default party size should be 4", 4, model.getValue());
    }

    @Test
    public void testPartySizeSpinnerStep() {
        JSpinner spinner = panel.getPartySizeSpinner();
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();

        assertEquals("Step size should be 1", 1, model.getStepSize());
    }

    @Test
    public void testParseTableResponseValid() {
        String response = "OK T1:4:Main,T2:6:Patio";
        String[] result = panel.parseTableResponse(response);

        assertEquals(2, result.length);
        assertEquals("Table T1 - Capacity: 4 - Section: Main", result[0]);
        assertEquals("Table T2 - Capacity: 6 - Section: Patio", result[1]);
    }

    @Test
    public void testParseTableResponseEmpty() {
        String response = "OK ";
        String[] result = panel.parseTableResponse(response);

        assertEquals(0, result.length);
    }

    @Test
    public void testParseTableResponseSingleTable() {
        String response = "OK Table1:8:VIP";
        String[] result = panel.parseTableResponse(response);

        assertEquals(1, result.length);
        assertEquals("Table Table1 - Capacity: 8 - Section: VIP", result[0]);
    }

    @Test
    public void testParseTableResponseError() {
        String response = "ERROR: not available";
        String[] result = panel.parseTableResponse(response);

        assertEquals(0, result.length);
    }

    @Test
    public void testParseTableResponseNull() {
        String[] result = panel.parseTableResponse(null);

        assertEquals(0, result.length);
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
    public void testClearTableList() {
        panel.getTableListModel().addElement("Test Table");
        assertEquals(1, panel.getTableListModel().size());

        panel.clearTableList();
        assertEquals(0, panel.getTableListModel().size());
    }

    @Test
    public void testSetStatus() {
        panel.setStatus("Test message");
        assertEquals("Test message", panel.getStatusLabel().getText());
    }

    @Test
    public void testRefreshClearsTableList() {
        panel.getTableListModel().addElement("Test Table");
        panel.refresh();
        assertEquals(0, panel.getTableListModel().size());
    }

    @Test
    public void testDateFieldExists() {
        JTextField dateField = panel.getDateField();
        assertNotNull("Date field should exist", dateField);
    }

    @Test
    public void testTimeFieldExists() {
        JTextField timeField = panel.getTimeField();
        assertNotNull("Time field should exist", timeField);
    }

    @Test
    public void testReservationIdFieldExists() {
        JTextField reservationIdField = panel.getReservationIdField();
        assertNotNull("Reservation ID field should exist", reservationIdField);
    }

    @Test
    public void testTableListExists() {
        JList<String> tableList = panel.getTableList();
        assertNotNull("Table list should exist", tableList);
    }

    @Test
    public void testTableListSingleSelection() {
        JList<String> tableList = panel.getTableList();
        assertEquals(ListSelectionModel.SINGLE_SELECTION, tableList.getSelectionMode());
    }
}
