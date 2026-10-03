import org.junit.Test;
import static org.junit.Assert.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * tests for config model
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class ConfigTest {

    @Test
    public void testConstructorAndGetters() {
        Map<DayOfWeek, Hours> hoursByDay = new HashMap<DayOfWeek, Hours>();
        Hours weekdayHours = new Hours(LocalTime.of(9, 0), LocalTime.of(21, 0));
        hoursByDay.put(DayOfWeek.MONDAY, weekdayHours);

        List<SectionLock> locks = new ArrayList<SectionLock>();

        Config config = new Config(hoursByDay, locks);

        assertEquals(hoursByDay, config.getHoursByDay());
        assertEquals(locks, config.getLocks());
    }

    @Test
    public void testMultipleDays() {
        Map<DayOfWeek, Hours> hoursByDay = new HashMap<DayOfWeek, Hours>();
        Hours weekdayHours = new Hours(LocalTime.of(9, 0), LocalTime.of(21, 0));
        Hours weekendHours = new Hours(LocalTime.of(10, 0), LocalTime.of(22, 0));

        hoursByDay.put(DayOfWeek.MONDAY, weekdayHours);
        hoursByDay.put(DayOfWeek.TUESDAY, weekdayHours);
        hoursByDay.put(DayOfWeek.SATURDAY, weekendHours);
        hoursByDay.put(DayOfWeek.SUNDAY, weekendHours);

        List<SectionLock> locks = new ArrayList<SectionLock>();
        Config config = new Config(hoursByDay, locks);

        assertEquals(4, config.getHoursByDay().size());
        assertEquals(weekdayHours, config.getHoursByDay().get(DayOfWeek.MONDAY));
        assertEquals(weekendHours, config.getHoursByDay().get(DayOfWeek.SATURDAY));
    }

    @Test
    public void testWithSectionLocks() {
        Map<DayOfWeek, Hours> hoursByDay = new HashMap<DayOfWeek, Hours>();

        List<SectionLock> locks = new ArrayList<SectionLock>();
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 12, 15), LocalTime.of(18, 0));
        SectionLock lock = new SectionLock(slot, "patio");
        locks.add(lock);

        Config config = new Config(hoursByDay, locks);

        assertEquals(1, config.getLocks().size());
        assertEquals("patio", config.getLocks().get(0).getSection());
    }

    @Test
    public void testToString() {
        Map<DayOfWeek, Hours> hoursByDay = new HashMap<DayOfWeek, Hours>();
        List<SectionLock> locks = new ArrayList<SectionLock>();
        Config config = new Config(hoursByDay, locks);

        String result = config.toString();
        assertTrue(result.contains("Config"));
        assertTrue(result.contains("hoursByDay"));
        assertTrue(result.contains("locks"));
    }

    @Test
    public void testImplementsInterface() {
        Map<DayOfWeek, Hours> hoursByDay = new HashMap<DayOfWeek, Hours>();
        List<SectionLock> locks = new ArrayList<SectionLock>();
        Config config = new Config(hoursByDay, locks);
        assertTrue(config instanceof ConfigInterface);
    }
}
