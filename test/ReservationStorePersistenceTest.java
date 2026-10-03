import org.junit.*;
import java.io.*;
import java.time.*;
import java.util.*;
import static org.junit.Assert.*;

/**
 * reservation store persistence test
 *
 * tests file creation, save and reload, updates, deletions
 * verifies data persists correctly to file
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class ReservationStorePersistenceTest {

    private static final String DATA_DIR = "data";
    private static final String RES_FILE = "data/reservations.txt";

    private static TimeSlot slot(int y, int m, int d, int hh, int mm) {
        return new TimeSlot(LocalDate.of(y, m, d), LocalTime.of(hh, mm));
    }
    private static Reservation res(String id, String user, String table, int n, TimeSlot s, Status st) {
        return new Reservation(id, user, table, n, s, st);
    }

    @Before
    public void cleanFile() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
        File f = new File(RES_FILE);
        if (f.exists()) f.delete();
    }

    @Test
    public void createsFileIfMissing() {
        assertFalse(new File(RES_FILE).exists());
        new FileReservationStore(); // ctor ensures file and dir exist
        assertTrue(new File(RES_FILE).exists());
    }

    @Test
    public void saveThenReloadContainsReservation() {
        TimeSlot s = slot(2025, 11, 7, 18, 0);
        Reservation r = res("r1", "king", "T4", 2, s, Status.BOOKED);

        FileReservationStore store1 = new FileReservationStore();
        store1.save(r);
        store1.flush();

        FileReservationStore store2 = new FileReservationStore();
        List<Reservation> after = store2.listBySlot(s);

        assertEquals(1, after.size());
        Reservation got = after.get(0);
        assertEquals("r1", got.getId());
        assertEquals("king", got.getUserId());
        assertEquals("T4", got.getTableId());
        assertEquals(2, got.getPartySize());
        assertEquals(Status.BOOKED, got.getStatus());
        assertEquals(s.getDate(), got.getSlot().getDate());
        assertEquals(s.getStart(), got.getSlot().getStart());
    }

    @Test
    public void upsertSameIdReplacesRecord() {
        TimeSlot s = slot(2025, 11, 7, 19, 30);
        FileReservationStore store = new FileReservationStore();

        store.save(res("r2", "bob", "T2", 2, s, Status.BOOKED));
        store.save(res("r2", "bob", "T6", 4, s, Status.BOOKED)); // same id, updated
        store.flush();

        FileReservationStore reload = new FileReservationStore();
        Optional<Reservation> got = reload.get("r2");
        assertTrue(got.isPresent());
        assertEquals("T6", got.get().getTableId());
        assertEquals(4, got.get().getPartySize());
    }

    @Test
    public void listBySlotIgnoresCancelled() {
        TimeSlot s = slot(2025, 11, 8, 12, 0);
        FileReservationStore store = new FileReservationStore();

        store.save(res("b1", "u1", "TA", 2, s, Status.BOOKED));
        store.save(res("c1", "u2", "TB", 3, s, Status.CANCELLED));
        store.flush();

        FileReservationStore reload = new FileReservationStore();
        List<Reservation> open = reload.listBySlot(s);
        assertEquals(1, open.size());
        assertEquals("b1", open.get(0).getId());
    }

    @Test
    public void getMissingAndDeleteMissingBehave() {
        FileReservationStore store = new FileReservationStore();
        assertFalse(store.get("nope").isPresent());
        assertFalse(store.delete("nope"));
    }

    @Test
    public void deleteThenReloadIsGone() {
        TimeSlot s = slot(2025, 11, 9, 13, 0);
        FileReservationStore store1 = new FileReservationStore();
        store1.save(res("r3", "dimitrious", "T8", 2, s, Status.BOOKED));
        store1.flush();

        assertTrue(store1.delete("r3"));
        store1.flush();

        FileReservationStore store2 = new FileReservationStore();
        assertTrue(store2.listBySlot(s).isEmpty());
        assertFalse(store2.get("r3").isPresent());
    }
}
