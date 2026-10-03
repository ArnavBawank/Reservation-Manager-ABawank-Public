import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * config interface
 *
 * defines config data access methods
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public interface ConfigInterface {
    Map<DayOfWeek, Hours> getHoursByDay();
    List<SectionLock> getLocks();
}
