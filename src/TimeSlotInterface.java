import java.time.LocalDate;
import java.time.LocalTime;

/**
 * time slot interface
 *
 * defines time slot data access methods
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public interface TimeSlotInterface {
    LocalDate getDate();
    LocalTime getStart();
}
