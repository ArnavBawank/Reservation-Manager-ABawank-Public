import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * tests for time slot model
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class TimeSlotTest {

    @Test
    public void testConstructorAndGetters() {
        LocalDate date = LocalDate.of(2025, 12, 15);
        LocalTime time = LocalTime.of(18, 30);
        TimeSlot slot = new TimeSlot(date, time);

        assertEquals(date, slot.getDate());
        assertEquals(time, slot.getStart());
    }

    @Test
    public void testTimeNormalization() {
        LocalDate date = LocalDate.of(2025, 12, 15);
        LocalTime timeWithSeconds = LocalTime.of(18, 30, 45, 123456789);
        TimeSlot slot = new TimeSlot(date, timeWithSeconds);

        LocalTime expected = LocalTime.of(18, 30, 0, 0);
        assertEquals(expected, slot.getStart());
    }

    @Test
    public void testEqualsTrue() {
        LocalDate date = LocalDate.of(2025, 12, 15);
        LocalTime time = LocalTime.of(18, 30);
        TimeSlot slot1 = new TimeSlot(date, time);
        TimeSlot slot2 = new TimeSlot(date, time);

        assertTrue(slot1.equals(slot2));
        assertTrue(slot2.equals(slot1));
    }

    @Test
    public void testEqualsFalseDifferentDate() {
        LocalTime time = LocalTime.of(18, 30);
        TimeSlot slot1 = new TimeSlot(LocalDate.of(2025, 12, 15), time);
        TimeSlot slot2 = new TimeSlot(LocalDate.of(2025, 12, 16), time);

        assertFalse(slot1.equals(slot2));
    }

    @Test
    public void testEqualsFalseDifferentTime() {
        LocalDate date = LocalDate.of(2025, 12, 15);
        TimeSlot slot1 = new TimeSlot(date, LocalTime.of(18, 30));
        TimeSlot slot2 = new TimeSlot(date, LocalTime.of(19, 30));

        assertFalse(slot1.equals(slot2));
    }

    @Test
    public void testEqualsWithNonTimeSlot() {
        LocalDate date = LocalDate.of(2025, 12, 15);
        LocalTime time = LocalTime.of(18, 30);
        TimeSlot slot = new TimeSlot(date, time);

        assertFalse(slot.equals("not a timeslot"));
        assertFalse(slot.equals(null));
    }

    @Test
    public void testHashCodeConsistency() {
        LocalDate date = LocalDate.of(2025, 12, 15);
        LocalTime time = LocalTime.of(18, 30);
        TimeSlot slot1 = new TimeSlot(date, time);
        TimeSlot slot2 = new TimeSlot(date, time);

        assertEquals(slot1.hashCode(), slot2.hashCode());
    }

    @Test
    public void testImplementsInterface() {
        LocalDate date = LocalDate.of(2025, 12, 15);
        LocalTime time = LocalTime.of(18, 30);
        TimeSlot slot = new TimeSlot(date, time);
        assertTrue(slot instanceof TimeSlotInterface);
    }
}
