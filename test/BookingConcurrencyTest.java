import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;

/**
 * booking concurrency test
 *
 * tests that only one booking succeeds per slot under concurrency
 * many threads try to book same slot with one table available
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class BookingConcurrencyTest {

    private static final String SEATING_FILE = "data/seating_concurrency_test.txt";
    private static final String RES_FILE = "data/reservations.txt";

    @Before
    public void cleanBefore() {
        new File(SEATING_FILE).delete();
        new File(RES_FILE).delete();
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
    }

    @After
    public void cleanAfter() {
        new File(SEATING_FILE).delete();
        new File(RES_FILE).delete();
    }

    @Test
    public void onlyOneBookingWinsPerSlot() throws Exception {
        // seating with exactly one table that fits
        FileSeatingStore seating = new FileSeatingStore(SEATING_FILE);
        for (Table t : new ArrayList<Table>(seating.listAll())) seating.remove(t.getId());
        seating.upsert(new Table("T2", 2, "Main"));

        ReservationStore store = new FileReservationStore();
        AvailabilityService avail = new AvailabilityServiceSimple(seating, store);
        BookingService booking = new BookingServiceSimple(avail, store);

        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 11, 7), LocalTime.of(18, 0));
        AtomicInteger success = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < 10; i++) {
            final int idx = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        booking.bookBestFit("u" + idx, 2, slot);
                        success.incrementAndGet();
                    } catch (NoAvailabilityException e) {
                        // expected for all but one thread
                    }
                }
            });
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) t.join();

        assertEquals(1, success.get());
        assertEquals(1, store.listBySlot(slot).size());
    }
}
