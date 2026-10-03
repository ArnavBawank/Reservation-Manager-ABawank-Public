import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * config model
 *
 * represents configuration with hours by day and section locks
 *
 * @author Arnav Bawank
 * @version Nov 10th, 2025
 */
public class Config implements ConfigInterface {

    private final Map<DayOfWeek, Hours> hoursByDay;
    private final List<SectionLock> locks;

    public Config(Map<DayOfWeek, Hours> hoursByDay, List<SectionLock> locks) {
        this.hoursByDay = hoursByDay;
        this.locks = locks;
    }

    public Map<DayOfWeek, Hours> getHoursByDay() {
        return hoursByDay;
    }

    public List<SectionLock> getLocks() {
        return locks;
    }

    @Override
    public String toString() {
        return "Config{" + "hoursByDay=" + hoursByDay + ", locks=" + locks + '}';
    }
}