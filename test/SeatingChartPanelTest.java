import org.junit.Test;
import static org.junit.Assert.*;

/**
 * tests for seating chart panel
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class SeatingChartPanelTest {

    @Test
    public void testConstructor() {
        SeatingChartPanel panel = new SeatingChartPanel();
        assertNotNull(panel);
    }

    @Test
    public void testSetAvailableTablesEmpty() {
        SeatingChartPanel panel = new SeatingChartPanel();
        panel.setAvailableTables(new String[0]);
        // no exception means success
    }

    @Test
    public void testSetAvailableTablesNull() {
        SeatingChartPanel panel = new SeatingChartPanel();
        panel.setAvailableTables(null);
        // no exception means success
    }

    @Test
    public void testSetAvailableTablesWithData() {
        SeatingChartPanel panel = new SeatingChartPanel();
        String[] tables = new String[] {
            "Table T1 - Capacity: 4 - Section: Main",
            "Table T2 - Capacity: 6 - Section: Patio"
        };
        panel.setAvailableTables(tables);
        // no exception means success
    }

    @Test
    public void testImplementsInterface() {
        SeatingChartPanel panel = new SeatingChartPanel();
        assertTrue(panel instanceof SeatingChartPanelInterface);
    }
}
