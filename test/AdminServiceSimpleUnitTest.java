import org.junit.Test;
import java.time.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * 
 * Test cases made for AdminServiceSimple.
 * Uses fake small stores because logic is what's being tested.
 * 
 * @author Arnav Bawankule
 * @version 24 Nov 2025
 * 
 */

public class AdminServiceSimpleUnitTest {

    /**
     * simple in-memory config store for tests
     */
    private static class FakeConfigStore implements ConfigStore {
        private Config savedCfg;

        @Override
        public Optional<Config> load() {
            return Optional.ofNullable(savedCfg);
        }

        @Override
        public void save(Config config) {
            this.savedCfg = config;
        }
    }

    /**
     * simple in-memory seating store for tests
     */
    private static class FakeSeatingStore implements SeatingStore {
        Map<String, Table> tables = new HashMap<>();

        @Override
        public List<Table> listAll() {
            return new ArrayList<>(tables.values());
        }

        @Override
        public Optional<Table> get(String id) {
            return Optional.ofNullable(tables.get(id));
        }

        @Override
        public void upsert(Table table) {
            tables.put(table.getId(), table);
        }

        @Override
        public boolean remove(String id) {
            return tables.remove(id) != null;
        }

        @Override
        public void flush() { }
    }

    @Test
    public void testSetHoursSaveConfig() {
        FakeConfigStore config = new FakeConfigStore();
        AdminService admin = new AdminServiceSimple(new FakeSeatingStore(), config);

        admin.setHours(DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0));

        Config saved = config.load().get();
        Hours h = saved.getHoursByDay().get(DayOfWeek.MONDAY);

        assertEquals(LocalTime.of(9, 0), h.getOpen());
        assertEquals(LocalTime.of(17, 0), h.getClose());
    }

    @Test(expected = ValidationException.class)
    public void testSetHoursInvalidRangeThrows() {
        AdminService admin = new AdminServiceSimple(
                new FakeSeatingStore(),
                new FakeConfigStore()
        );

        admin.setHours(DayOfWeek.MONDAY,
                LocalTime.of(18, 0),
                LocalTime.of(10, 0));
    }

    @Test
    public void testUpsertTableStoresTable() {
        FakeSeatingStore seatingStore = new FakeSeatingStore();
        AdminService admin = new AdminServiceSimple(seatingStore, new FakeConfigStore());

        admin.upsertTable(5, 4, "Main");

        Table t = seatingStore.tables.get("5");
        assertNotNull(t);
        assertEquals("Main", t.getSection());
        assertEquals(4, t.getCapacity());
    }

    @Test
    public void testLockSectionSavesToConfig() {
        FakeConfigStore config = new FakeConfigStore();
        AdminService admin = new AdminServiceSimple(new FakeSeatingStore(), config);

        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.NOON);
        admin.lockSection(slot, "Patio");

        SectionLock lock = config.load().get().getLocks().get(0);

        assertEquals(slot.getDate(), lock.getSlot().getDate());
        assertEquals("Patio", lock.getSection());
    }
}
