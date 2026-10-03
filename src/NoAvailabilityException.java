/**
 * no availability exception
 *
 * thrown when no tables are available for booking at requested time slot
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class NoAvailabilityException extends Exception {
    public NoAvailabilityException(String message) {
        super(message);
    }
}
