import java.time.LocalTime;

/**
 * hours model
 *
 * represents opening and closing times for a day
 *
 * @author Arnav Bawank
 * @version Nov 10th, 2025
 */
public class Hours implements HoursInterface {
    public final LocalTime open;
    public final LocalTime close;

    public Hours(LocalTime open, LocalTime close) {
        this.open = open;
        this.close = close;
    }

    public LocalTime getOpen() {
        return open;
    }

    public LocalTime getClose() {
        return close;
    }

    @Override
    public String toString() {
        return "Hours{" + "open=" + open + ", close=" + close + '}';
    }
}