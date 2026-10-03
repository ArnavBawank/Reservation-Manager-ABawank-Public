import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * tests for section lock model
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class SectionLockTest {

    @Test
    public void testConstructorAndGetters() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));
        SectionLock lock = new SectionLock(slot, "patio");

        assertEquals(slot, lock.getSlot());
        assertEquals("patio", lock.getSection());
    }

    @Test
    public void testDifferentSections() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));

        SectionLock patioLock = new SectionLock(slot, "patio");
        SectionLock indoorLock = new SectionLock(slot, "indoor");

        assertEquals("patio", patioLock.getSection());
        assertEquals("indoor", indoorLock.getSection());
    }

    @Test
    public void testDifferentTimeSlots() {
        TimeSlot slot1 = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));
        TimeSlot slot2 = new TimeSlot(LocalDate.of(2025, 12, 16), LocalTime.of(19, 0));

        SectionLock lock1 = new SectionLock(slot1, "patio");
        SectionLock lock2 = new SectionLock(slot2, "patio");

        assertEquals(slot1, lock1.getSlot());
        assertEquals(slot2, lock2.getSlot());
    }

    @Test
    public void testToString() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));
        SectionLock lock = new SectionLock(slot, "patio");

        String result = lock.toString();
        assertTrue(result.contains("SectionLock"));
        assertTrue(result.contains("slot"));
        assertTrue(result.contains("patio"));
    }

    @Test
    public void testImplementsInterface() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));
        SectionLock lock = new SectionLock(slot, "patio");
        assertTrue(lock instanceof SectionLockInterface);
    }
}
