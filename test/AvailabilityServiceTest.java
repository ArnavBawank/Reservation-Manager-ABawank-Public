import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.time.*;
import java.util.*;


/**
 * availability service tests
 *
 * uses FileSeatingStore with a test seating file and FileReservationStore
 * open tables ordering, booked table exclusion, capacity filtering, and input validation for party size
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class AvailabilityServiceTest {

    private static final String SEATING_FILE = "data/availability_seating_test.txt";
    private static final String RES_FILE = "data/reservations.txt";

    // clean test files so each test starts fresh
    @Before
    public void cleanFiles() {
        new File(SEATING_FILE).delete();
        new File(RES_FILE).delete();
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
    }
    private FileSeatingStore seatingWith(Table... tables) {
        FileSeatingStore seating = new FileSeatingStore(SEATING_FILE);
        // remove any defaults loaded by FileSeatingStore
        List<Table> existing = new ArrayList<Table>(seating.listAll());
        for (Table t : existing) seating.remove(t.getId());
        // add the tables we want for this test
        for (Table t : tables) seating.upsert(t);
        seating.flush();
        return seating;
    }

    @Test
    public void noReservationsAllTablesOpenSortedByCapacityThenId() {
        // seating: T6(6), T2(2), T4(4) -> expect order T2, T4, T6
        FileSeatingStore seating = seatingWith(
                new Table("T6", 6, "C"),
                new Table("T2", 2, "A"),
                new Table("T4", 4, "B")
        );
        ReservationStore store = new FileReservationStore();
        AvailabilityService svc = new AvailabilityServiceSimple(seating, store);

        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 11, 7), LocalTime.of(18, 0));
        List<Table> open = svc.listOpen(slot);

        assertEquals(3, open.size());
        assertEquals("T2", open.get(0).getId());
        assertEquals("T4", open.get(1).getId());
        assertEquals("T6", open.get(2).getId());
    }

    @Test
    public void oneBookedIsExcludedFromOpen() {
        // seating: T2(2), T4(4), T6(6) ; book T4 at this slot
        FileSeatingStore seating = seatingWith(
                new Table("T2", 2, "A"),
                new Table("T4", 4, "B"),
                new Table("T6", 6, "C")
        );
        ReservationStore store = new FileReservationStore();
        AvailabilityService svc = new AvailabilityServiceSimple(seating, store);

        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 11, 7), LocalTime.of(19, 0));
        store.save(new Reservation("r1", "alice", "T4", 3, slot, Status.BOOKED));
        store.flush();

        List<Table> open = svc.listOpen(slot);
        assertEquals(2, open.size());
        // remaining should be T2 then T6 (still sorted by capacity then id)
        assertEquals("T2", open.get(0).getId());
        assertEquals("T6", open.get(1).getId());
    }

    @Test
    public void listOpenForPartyFiltersByCapacityAndKeepsOrder() {
        // seating: T2(2), T4(4), T6(6) ; party size 3 -> expect T4, T6
        FileSeatingStore seating = seatingWith(
                new Table("T2", 2, "A"),
                new Table("T4", 4, "B"),
                new Table("T6", 6, "C")
        );
        ReservationStore store = new FileReservationStore();
        AvailabilityService svc = new AvailabilityServiceSimple(seating, store);

        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 11, 8), LocalTime.of(18, 30));
        List<Table> fit = svc.listOpenForParty(slot, 3);

        assertEquals(2, fit.size());
        assertEquals("T4", fit.get(0).getId());
        assertEquals("T6", fit.get(1).getId());
    }

    @Test
    public void tieOnCapacitySortsById() {
        // seating: A1(4), B2(4), T2(2) -> expect T2, A1, B2
        FileSeatingStore seating = seatingWith(
                new Table("B2", 4, "B"),
                new Table("A1", 4, "A"),
                new Table("T2", 2, "T")
        );
        ReservationStore store = new FileReservationStore();
        AvailabilityService svc = new AvailabilityServiceSimple(seating, store);

        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 11, 9), LocalTime.of(12, 0));
        List<Table> open = svc.listOpen(slot);

        assertEquals(3, open.size());
        assertEquals("T2", open.get(0).getId()); // smallest capacity first
        assertEquals("A1", open.get(1).getId()); 
        assertEquals("B2", open.get(2).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void partySizeMustBePositive() {
        FileSeatingStore seating = seatingWith(new Table("T2", 2, "A"));
        ReservationStore store = new FileReservationStore();
        AvailabilityService svc = new AvailabilityServiceSimple(seating, store);

        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 11, 10), LocalTime.of(18, 0));
        svc.listOpenForParty(slot, 0); // should throw
    }
}
