import java.time.*;

/**
 * time slot model
 *
 * combines date and time for reservations
 * time normalized to minutes (seconds/nanos removed)
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class TimeSlot implements TimeSlotInterface {
    private LocalDate date;
    private LocalTime start;

    public TimeSlot(LocalDate date, LocalTime start) {
        this.date = date;
        this.start = start.withSecond(0).withNano(0);
    }

    public LocalDate getDate() { return date; }
    public LocalTime getStart() { return start; }
    
    // checks if two timeslots have the same date and time
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TimeSlot)) {
            return false;
        }
        TimeSlot other = (TimeSlot) o;
        return date.equals(other.date) && start.equals(other.start);
    }
    
    // converts date and time into a number so we can compare timeslots quickly, hashcode stuff works somehow
    @Override
    public int hashCode() {
        return date.hashCode() + start.hashCode();
    }
}
