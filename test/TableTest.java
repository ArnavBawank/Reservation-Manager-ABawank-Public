import org.junit.Test;
import static org.junit.Assert.*;

/**
 * tests for table model
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class TableTest {

    @Test
    public void testConstructorAndGetters() {
        Table table = new Table("t1", 4, "patio");
        assertEquals("t1", table.getId());
        assertEquals(4, table.getCapacity());
        assertEquals("patio", table.getSection());
    }

    @Test
    public void testDifferentCapacities() {
        Table small = new Table("t1", 2, "indoor");
        Table large = new Table("t2", 10, "outdoor");

        assertEquals(2, small.getCapacity());
        assertEquals(10, large.getCapacity());
    }

    @Test
    public void testDifferentSections() {
        Table patio = new Table("t1", 4, "patio");
        Table indoor = new Table("t2", 4, "indoor");

        assertEquals("patio", patio.getSection());
        assertEquals("indoor", indoor.getSection());
    }

    @Test
    public void testImplementsInterface() {
        Table table = new Table("t1", 4, "section");
        assertTrue(table instanceof TableInterface);
    }
}
