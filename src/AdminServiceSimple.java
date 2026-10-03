import java.util.*;
import java.time.*;

/**
 * admin service implementation
 *
 * uses seating store and config store
 * mutates hours, locks, and seating configuration
 *
 * @author Arnav Bawank
 * @version Nov 25th, 2025
 */
public class AdminServiceSimple implements AdminService {

    private final SeatingStore seatingStore;
    private final ConfigStore configStore;
    private static final Object LOCK = new Object();

    public AdminServiceSimple(SeatingStore seatingStore, ConfigStore configStore) {
        this.seatingStore = seatingStore;
        this.configStore = configStore;
    }

    @Override
    public void setHours(DayOfWeek day, LocalTime open, LocalTime close) {

        if (day == null || open == null || close == null) {
            throw new ValidationException("day/open/close must not be null.");
        }

        if (open.isAfter(close)) {
            throw new ValidationException("open time must be before or equal to the close time.");
        }

        synchronized (LOCK) {
            Optional<Config> opt = configStore.load();
            Config cfg;

            if (opt.isPresent()) {
                cfg = opt.get();
            } else {
                cfg = new Config(new EnumMap<>(DayOfWeek.class), new ArrayList<>());
            }

            // update or add hours
            cfg.getHoursByDay().put(day, new Hours(open, close));

            //save
            configStore.save(cfg);

            System.out.println("Set hours for " + day + ": " + open + " - " + close);
        }
    }

    @Override
    public void lockSection(TimeSlot slot, String section) {

        if (slot == null || section == null || section.isBlank()) {
            throw new ValidationException("slot or section cannot be blank/null.");
        }

        synchronized (LOCK) {
            Optional<Config> opt = configStore.load();
            Config cfg;

            if (opt.isPresent()) {
                cfg = opt.get();
            } else {
                cfg = new Config(new EnumMap<>(DayOfWeek.class), new ArrayList<>());
            }

            cfg.getLocks().add(new SectionLock(slot, section));
            configStore.save(cfg);
            System.out.println("Locked section " + section + " at " + slot);
        }
    }


    @Override
    public void upsertTable(int tableId, int capacity, String section) {
        if (tableId <= 0 || capacity <= 0) {
            throw new ValidationException("TableID and capacity must be positive.");
        }

        if (section == null || section.isBlank()) {
            throw new ValidationException("Section cannot be blank.");
        }

        Table t = new Table(String.valueOf(tableId), capacity, section);
        seatingStore.upsert(t);
        System.out.println("Updated/added table: " + t);
    }

}