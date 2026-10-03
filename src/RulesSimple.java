import java.time.*;
import java.util.*;

/**
 * rules implementation
 *
 * uses config store to validate reservations
 * enforces 7 day horizon, within open hours, section not locked
 *
 * @author Arnav Bawank
 * @version Nov 24th, 2025
 */
public class RulesSimple implements Rules {
    private final ConfigStore configStore;

    public RulesSimple(ConfigStore configStore) {
        this.configStore = configStore;
    }

    @Override
    public void validate(String userId, TimeSlot slot, int partySize, Table chosen) throws ValidationException {

        if (slot == null) {
            throw new ValidationException("TimeSlot required.");
        }

        if (partySize <= 0) {
            throw new ValidationException("Party size must be positive.");
        }

        if (chosen == null) {
            throw new ValidationException("Table must be selected.");
        }

        Optional<Config> optionalCfg = configStore.load();
        if (optionalCfg.isEmpty()) {
            throw new ValidationException("Configuration not found - cannot validate.");
        }

        Config cfg = optionalCfg.get();
        LocalDate today = LocalDate.now();
        LocalDate date = slot.getDate();
        LocalTime start = slot.getStart();

        //check 7 day booking limit
        if (date.isBefore(today) || date.isAfter(today.plusDays(7))) {
            throw new ValidationException("Cannot book more than 7 days in advance.");
        }

        //check open hours
        DayOfWeek day = date.getDayOfWeek();
        Hours hours = cfg.getHoursByDay().get(day);

        if (hours == null) {
            throw new ValidationException("No hours set for " + day + ".");
        }

        if (start.isBefore(hours.getOpen()) || start.isAfter(hours.getClose())) {
            throw new ValidationException("Requested time is outside business hours.");
        }

        //check for locked sections
        for (SectionLock lock : cfg.getLocks()) {
            if (lock.getSlot().getDate().equals(date)
                && lock.getSlot().getStart().equals(start)
                && lock.getSection().equalsIgnoreCase(chosen.getSection())) {
                throw new ValidationException("This section is locked for that time slot.");
            }
        }
    }
}