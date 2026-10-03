/**
 * booking service interface
 *
 * books best fit table for party size at time slot
 * cancels reservations by id
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public interface BookingService {
    Reservation bookBestFit(String userId, int partySize, TimeSlot slot)throws NoAvailabilityException;

    boolean cancel(String reservationId);
}
