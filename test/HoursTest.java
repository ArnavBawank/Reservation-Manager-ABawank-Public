import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalTime;

/**
 * tests for hours model
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class HoursTest {

    @Test
    public void testConstructorAndGetters() {
        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.of(21, 0);
        Hours hours = new Hours(open, close);

        assertEquals(open, hours.getOpen());
        assertEquals(close, hours.getClose());
    }

    @Test
    public void testDifferentHours() {
        Hours morning = new Hours(LocalTime.of(6, 0), LocalTime.of(12, 0));
        Hours evening = new Hours(LocalTime.of(17, 0), LocalTime.of(23, 0));

        assertEquals(LocalTime.of(6, 0), morning.getOpen());
        assertEquals(LocalTime.of(12, 0), morning.getClose());
        assertEquals(LocalTime.of(17, 0), evening.getOpen());
        assertEquals(LocalTime.of(23, 0), evening.getClose());
    }

    @Test
    public void testToString() {
        Hours hours = new Hours(LocalTime.of(9, 0), LocalTime.of(21, 0));
        String result = hours.toString();

        assertTrue(result.contains("Hours"));
        assertTrue(result.contains("open"));
        assertTrue(result.contains("close"));
    }

    @Test
    public void testImplementsInterface() {
        Hours hours = new Hours(LocalTime.of(9, 0), LocalTime.of(21, 0));
        assertTrue(hours instanceof HoursInterface);
    }
}
