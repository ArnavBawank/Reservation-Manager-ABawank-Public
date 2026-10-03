import java.util.*;

/**
 * booking service implementation
 *
 * uses availability service and reservation store
 * per slot locking prevents double bookings
 * picks smallest table that fits party size
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class BookingServiceSimple implements BookingService {

    private final AvailabilityService availability;
    private final ReservationStore reservations;

    // per slot lock registry (simple list + a guard, no HashMap needed)
    private static final Object LOCKS_GUARD = new Object();
    private static final List<SlotLock> LOCKS = new ArrayList<SlotLock>();
    private final Random rand = new Random();

    /**
     * slot lock helper class
     *
     * holds lock object for a specific time slot key
     */
    private static final class SlotLock {
        final String key;
        final Object lock;
        SlotLock(String key, Object lock) {
            this.key = key;
            this.lock = lock;
        }
    }

    public BookingServiceSimple(AvailabilityService availability, ReservationStore reservations) {
        if (availability == null || reservations == null) {
            throw new IllegalArgumentException("availability and reservations are required");
        }
        this.availability = availability;
        this.reservations = reservations;
    }

    @Override
    public Reservation bookBestFit(String userId, int partySize, TimeSlot slot) throws NoAvailabilityException {
        if (slot == null) throw new IllegalArgumentException("slot is required");
        if (partySize <= 0) throw new IllegalArgumentException("partySize must be > 0");
        if (userId == null) userId = "";

        Object slotLock = lockFor(slot);
        synchronized (slotLock) {
            // recompute availability inside the slot lock to avoid races
            List<Table> open = availability.listOpenForParty(slot, partySize);
            if (open.isEmpty()) {
                throw new NoAvailabilityException("no table available for this slot");
            }
            // open is already sorted by capacity then id; pick the first (best fit)
            Table chosen = open.get(0);

            // make unique numeric id for reservation
            String id = String.valueOf(nextId());

            Reservation r = new Reservation(id, userId, chosen.getId(), partySize, slot, Status.BOOKED);
            return reservations.save(r);
        }
    }

    @Override
    public boolean cancel(String reservationId) {
        if (reservationId == null) return false;
        return reservations.delete(reservationId);
    }

    private Object lockFor(TimeSlot slot) {
        String k = slotKey(slot);
        synchronized (LOCKS_GUARD) {
            for (int i = 0; i < LOCKS.size(); i++) {
                SlotLock sl = LOCKS.get(i);
                if (sl.key.equals(k)) return sl.lock;
            }
            Object obj = new Object();
            LOCKS.add(new SlotLock(k, obj));
            return obj;
        }
    }

    private String slotKey(TimeSlot s) {
        // ensure seconds/nanos dont creep in
        java.time.LocalTime t = s.getStart().withSecond(0).withNano(0);
        String hhmm = String.format("%02d:%02d", t.getHour(), t.getMinute());
        return s.getDate().toString() + "@" + hhmm;
    }

    private int nextId() {
        for (int i = 0; i < 5; i++) {
            int id = 10_000_000 + rand.nextInt(90_000_000);
            if (!reservations.get(String.valueOf(id)).isPresent()) {
                return id;
            }
        }
        return 10_000_000 + rand.nextInt(90_000_000);
    }
}
