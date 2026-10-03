import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * tests for booking command helper
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class BookingCommandHelperTest {

    @Test
    public void testParseSlotValid() {
        TimeSlot slot = BookingCommandHelper.parseSlot("2025-12-15", "18:30");
        assertEquals(LocalDate.of(2025, 12, 15), slot.getDate());
        assertEquals(LocalTime.of(18, 30), slot.getStart());
    }

    @Test
    public void testParseSlotNormalizesSeconds() {
        TimeSlot slot = BookingCommandHelper.parseSlot("2025-12-15", "18:30:45");
        assertEquals(LocalTime.of(18, 30, 0), slot.getStart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSlotNullDate() {
        BookingCommandHelper.parseSlot(null, "18:30");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSlotNullTime() {
        BookingCommandHelper.parseSlot("2025-12-15", null);
    }

    @Test
    public void testParsePartySizeValid() {
        assertEquals(4, BookingCommandHelper.parsePartySize("4"));
        assertEquals(1, BookingCommandHelper.parsePartySize("1"));
        assertEquals(10, BookingCommandHelper.parsePartySize("10"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsePartySizeNull() {
        BookingCommandHelper.parsePartySize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsePartySizeZero() {
        BookingCommandHelper.parsePartySize("0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsePartySizeNegative() {
        BookingCommandHelper.parsePartySize("-1");
    }

    @Test(expected = NumberFormatException.class)
    public void testParsePartySizeInvalid() {
        BookingCommandHelper.parsePartySize("abc");
    }
}
