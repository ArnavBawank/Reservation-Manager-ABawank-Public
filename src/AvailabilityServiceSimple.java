import java.util.*;

/**
 * availability service implementation
 *
 * uses seating store and reservation store
 * computes open tables by subtracting booked tables from all tables
 * sorts by capacity then id
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class AvailabilityServiceSimple implements AvailabilityService {
    private final ReservationStore reservations;
    private final SeatingStore seating;
    public AvailabilityServiceSimple(SeatingStore seating, ReservationStore reservations) {
        this.seating = seating;
        this.reservations = reservations;
    }
    
    @Override
    public List<Table> listOpen(TimeSlot slot) {
        // get all tables
        List<Table> all = new ArrayList<Table>(seating.listAll());
    
        // collect tableIds that are already booked at this slot
        List<Reservation> booked = reservations.listBySlot(slot);
        List<String> taken = new ArrayList<String>();
        for (Reservation r : booked) {
            taken.add(r.getTableId());
        }
    
        // filter out taken tables
        List<Table> out = new ArrayList<Table>();
        for (Table t : all) {
            boolean isTaken = false;
            for (String id : taken) {
                if (id.equals(t.getId())) {
                    isTaken = true;
                    break;
                }
            }
            if (!isTaken) out.add(t);
        }
    
        // sort
        selectionSortByCapacityThenId(out);
        return out;
    }
    
    @Override
    public List<Table> listOpenForParty(TimeSlot slot, int partySize) {
        if (partySize <= 0) throw new IllegalArgumentException("partySize must be > 0");
        List<Table> open = listOpen(slot);
        List<Table> fit = new ArrayList<Table>();
        for (Table t : open) {
            if (t.getCapacity() >= partySize) fit.add(t);
        }
        // already sorted because listOpen() sorted it
        return fit;
    }
    
    // selection sort by capacity asc, then id asc
    private void selectionSortByCapacityThenId(List<Table> list) {
        // quick double for loop
        for (int i = 0; i < list.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < list.size(); j++) {
                Table a = list.get(j);
                Table b = list.get(min);
                boolean smallerCapacity = a.getCapacity() < b.getCapacity();
                boolean sameCapacityAndLowerId = a.getCapacity() == b.getCapacity()
                        && a.getId().compareTo(b.getId()) < 0;
                if (smallerCapacity || sameCapacityAndLowerId) {
                    min = j;
                }
            }
            if (min != i) {
                Table tmp = list.get(i);
                list.set(i, list.get(min));
                list.set(min, tmp);
            }
        }
    }
}