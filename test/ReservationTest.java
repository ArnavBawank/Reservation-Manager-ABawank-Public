import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * tests for reservation model
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class ReservationTest {

    @Test
    public void testConstructorAndGetters() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));
        Reservation res = new Reservation("r1", "u1", "t1", 4, slot, Status.BOOKED);

        assertEquals("r1", res.getId());
        assertEquals("u1", res.getUserId());
        assertEquals("t1", res.getTableId());
        assertEquals(4, res.getPartySize());
        assertEquals(slot, res.getSlot());
        assertEquals(Status.BOOKED, res.getStatus());
    }

    @Test
    public void testDifferentStatuses() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));

        Reservation booked = new Reservation("r1", "u1", "t1", 4, slot, Status.BOOKED);
        Reservation cancelled = new Reservation("r2", "u1", "t1", 4, slot, Status.CANCELLED);

        assertEquals(Status.BOOKED, booked.getStatus());
        assertEquals(Status.CANCELLED, cancelled.getStatus());
    }

    @Test
    public void testDifferentPartySizes() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));

        Reservation small = new Reservation("r1", "u1", "t1", 2, slot, Status.BOOKED);
        Reservation large = new Reservation("r2", "u1", "t2", 8, slot, Status.BOOKED);

        assertEquals(2, small.getPartySize());
        assertEquals(8, large.getPartySize());
    }

    @Test
    public void testImplementsInterface() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));
        Reservation res = new Reservation("r1", "u1", "t1", 4, slot, Status.BOOKED);
        assertTrue(res instanceof ReservationInterface);
    }
}
