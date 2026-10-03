import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * admin service interface
 *
 * sets hours of operation for days of week
 * locks sections for specific time slots
 * updates or adds tables to seating
 *
 * @author Arnav Bawank
 * @version Nov 10th, 2025
 */
public interface AdminService {

    //Allows admin to set the opening and closing hours for the respective day of the week.
    void setHours(DayOfWeek day, LocalTime open, LocalTime close);

    //Locks an entire section for a specific timeslot.
    void lockSection(TimeSlot slot, String section);

    //Updates a table with a new seating layout.
    void upsertTable(int tableId, int capacity, String section);
}