import org.junit.Test;
import java.time.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * 
 * Test cases made for RulesSimple
 * Uses fake small stores because logic is what's being tested.
 * 
 * @author Arnav Bawankule
 * @version 24 Nov 2025
 * 
 */

public class RulesSimpleUnitTest {

    /**
     * simple in-memory config store for tests
     */
    private static class FakeConfigStore implements ConfigStore {
        private Config savedCfg;

        FakeConfigStore(Config initialCfg) {
            this.savedCfg = initialCfg;
        }

        public Optional<Config> load() {
            return Optional.ofNullable(savedCfg);
        }

        public void save(Config config) {
            this.savedCfg = config;
        }
    }

    @Test(expected = ValidationException.class)
    public void testNullSlotThrows() {
        ConfigStore store = new FakeConfigStore(null);
        Rules rules = new RulesSimple(store);

        rules.validate("u1", null, 4, new Table("1", 4, "Main")); 
    }

    @Test(expected = ValidationException.class)
    public void testPartySizeZeroThrows() {
        ConfigStore store = new FakeConfigStore(null);
        Rules rules = new RulesSimple(store);

        rules.validate("u1", 
                        new TimeSlot(LocalDate.now(), LocalTime.NOON),
                        0,
                        new Table("1", 4, "Main")
        );
    }

    @Test(expected = ValidationException.class)
    public void testLockedSecThrows() {
        LocalDate today = LocalDate.now();

        Map<DayOfWeek, Hours> hours = new EnumMap<>(DayOfWeek.class);
        hours.put(today.getDayOfWeek(), new Hours(LocalTime.MIN, LocalTime.MAX));

        TimeSlot slot = new TimeSlot(today, LocalTime.NOON);

        //lock the slot
        List<SectionLock> locks = new ArrayList<>();
        locks.add(new SectionLock(slot, "Main"));

        Config cfg = new Config(hours, locks);
        Rules rules = new RulesSimple(new FakeConfigStore(cfg));

        rules.validate("u1", slot, 4, new Table("1", 4, "Main"));
    }

    @Test
    public void testValidReservationPasses() {
        LocalDate today = LocalDate.now();

        Map<DayOfWeek, Hours> hours = new EnumMap<>(DayOfWeek.class);
        hours.put(today.getDayOfWeek(), new Hours(LocalTime.MIN, LocalTime.MAX));

        List<SectionLock> locks = new ArrayList<>();

        Config cfg = new Config(hours, locks);
        Rules rules = new RulesSimple(new FakeConfigStore(cfg));

        // Should NOT throw
        rules.validate(
                "u1",
                new TimeSlot(today, LocalTime.NOON),
                4,
                new Table("1", 4, "Main")
        );
    }
}
