import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.File;
import java.time.*;
import java.util.*;

/**
 * rules test
 *
 * tests rejection of bookings more than 7 days out
 * tests rejection of bookings outside hours
 * tests rejection of bookings in locked sections
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class RulesTest {

    private static final String CONFIG_FILE = "data/config.txt";
    private FileConfigStore configStore;
    private RulesSimple rules;

    @Before
    public void setUp() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) file.delete();
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();

        FileUtil util = new FileUtil();
        configStore = new FileConfigStore(util);
        rules = new RulesSimple(configStore);
    }

    @After
    public void tearDown() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) file.delete();
    }

    /**
     * tests that bookings more than 7 days in advance are rejected
     */
    @Test(expected = ValidationException.class)
    public void rejectsBookingMoreThan7DaysOut() throws ValidationException {
        Map<DayOfWeek, Hours> hours = new EnumMap<>(DayOfWeek.class);
        hours.put(DayOfWeek.MONDAY, new Hours(LocalTime.of(9, 0), LocalTime.of(21, 0)));
        configStore.save(new Config(hours, new ArrayList<>()));

        TimeSlot slot = new TimeSlot(LocalDate.now().plusDays(8), LocalTime.of(18, 0));
        Table t = new Table("T1", 4, "A");
        rules.validate("user1", slot, 4, t);
    }

    /**
     * tests that bookings outside business hours are rejected
     */
    @Test(expected = ValidationException.class)
    public void rejectsBookingOutsideHours() throws ValidationException {
        Map<DayOfWeek, Hours> hours = new EnumMap<>(DayOfWeek.class);
        hours.put(DayOfWeek.MONDAY, new Hours(LocalTime.of(9, 0), LocalTime.of(21, 0)));
        configStore.save(new Config(hours, new ArrayList<>()));

        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(22, 0));
        Table t = new Table("T1", 4, "A");
        rules.validate("user1", slot, 4, t);
    }

    /**
     * tests that bookings in locked sections are rejected
     */
    @Test(expected = ValidationException.class)
    public void rejectsBookingInLockedSection() throws ValidationException {
        Map<DayOfWeek, Hours> hours = new EnumMap<>(DayOfWeek.class);
        hours.put(LocalDate.now().getDayOfWeek(), new Hours(LocalTime.of(9, 0), LocalTime.of(21, 0)));
        List<SectionLock> locks = new ArrayList<>();
        locks.add(new SectionLock(new TimeSlot(LocalDate.now(), LocalTime.of(18, 0)), "A"));
        configStore.save(new Config(hours, locks));

        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(18, 0));
        Table t = new Table("T1", 4, "A");
        rules.validate("user1", slot, 4, t);
    }

    /**
     * tests that valid bookings within hours and not in locked sections are allowed
     */
    @Test
    public void allowsValidBooking() throws ValidationException {
        Map<DayOfWeek, Hours> hours = new EnumMap<>(DayOfWeek.class);
        hours.put(LocalDate.now().getDayOfWeek(), new Hours(LocalTime.of(9, 0), LocalTime.of(21, 0)));
        configStore.save(new Config(hours, new ArrayList<>()));

        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(18, 0));
        Table t = new Table("T1", 4, "B");
        rules.validate("user1", slot, 4, t);
    }
}
