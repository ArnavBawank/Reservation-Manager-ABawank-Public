import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.time.*;
import java.util.*;

/**
 * admin service test
 *
 * tests that hours and locks persist and affect rules
 * tests that seating updates are visible
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class AdminServiceTest {

    private static final String SEATING_FILE = "data/seating_admin_test.txt";
    private static final String CONFIG_FILE = "data/config.txt";
    private FileSeatingStore seatingStore;
    private FileConfigStore configStore;
    private AdminServiceSimple admin;

    @Before
    public void setUp() {
        File seatingFile = new File(SEATING_FILE);
        if (seatingFile.exists()) seatingFile.delete();
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) configFile.delete();
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();

        seatingStore = new FileSeatingStore(SEATING_FILE);
        FileUtil util = new FileUtil();
        configStore = new FileConfigStore(util);
        admin = new AdminServiceSimple(seatingStore, configStore);
    }

    @After
    public void tearDown() {
        File seatingFile = new File(SEATING_FILE);
        if (seatingFile.exists()) seatingFile.delete();
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) configFile.delete();
    }

    /**
     * tests that setting hours persists to config store
     */
    @Test
    public void setHoursPersists() {
        DayOfWeek day = DayOfWeek.MONDAY;
        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.of(21, 0);
        admin.setHours(day, open, close);

        Optional<Config> cfgOpt = configStore.load();
        assertTrue(cfgOpt.isPresent());
        Config cfg = cfgOpt.get();
        Hours hours = cfg.getHoursByDay().get(day);
        assertNotNull(hours);
        assertEquals(open, hours.getOpen());
        assertEquals(close, hours.getClose());
    }

    /**
     * tests that locking a section persists to config store
     */
    @Test
    public void lockSectionPersists() {
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(18, 0));
        String section = "Main";
        admin.lockSection(slot, section);

        Optional<Config> cfgOpt = configStore.load();
        assertTrue(cfgOpt.isPresent());
        Config cfg = cfgOpt.get();
        List<SectionLock> locks = cfg.getLocks();
        assertEquals(1, locks.size());
        SectionLock lock = locks.get(0);
        assertEquals(slot.getDate(), lock.getSlot().getDate());
        assertEquals(slot.getStart(), lock.getSlot().getStart());
        assertEquals(section, lock.getSection());
    }

    /**
     * tests that upserting a table works and persists to seating store
     */
    @Test
    public void upsertTableWorks() {
        admin.upsertTable(10, 4, "Main");
        seatingStore.flush();

        Optional<Table> got = seatingStore.get("10");
        assertTrue(got.isPresent());
        assertEquals("10", got.get().getId());
        assertEquals(4, got.get().getCapacity());
        assertEquals("Main", got.get().getSection());
    }

    /**
     * tests that config can be loaded after saving
     */
    @Test
    public void configLoadsAfterSaving() {
        DayOfWeek day = DayOfWeek.TUESDAY;
        admin.setHours(day, LocalTime.of(10, 0), LocalTime.of(22, 0));

        Optional<Config> cfgOpt = configStore.load();
        assertTrue(cfgOpt.isPresent());
        Config cfg = cfgOpt.get();
        assertNotNull(cfg.getHoursByDay());
        assertNotNull(cfg.getLocks());
    }
}
