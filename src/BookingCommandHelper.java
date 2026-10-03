import java.time.LocalDate;
import java.time.LocalTime;

/**
 * booking command helper
 *
 * parses date, time, and party size strings for booking commands
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public class BookingCommandHelper implements BookingCommandHelperInterface {

    public static TimeSlot parseSlot(String dateStr, String timeStr) {
        if (dateStr == null || timeStr == null) {
            throw new IllegalArgumentException("date and time required");
        }
        LocalDate d = LocalDate.parse(dateStr.trim());
        LocalTime t = LocalTime.parse(timeStr.trim()).withSecond(0).withNano(0);
        return new TimeSlot(d, t);
    }

    public static int parsePartySize(String sizeStr) {
        if (sizeStr == null) throw new IllegalArgumentException("party size missing");
        int n = Integer.parseInt(sizeStr.trim());
        if (n <= 0) throw new IllegalArgumentException("party size must be > 0");
        return n;
    }
}
