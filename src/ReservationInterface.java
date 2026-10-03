/**
 * reservation interface
 *
 * defines reservation data access methods
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public interface ReservationInterface {
    String getId();
    String getUserId();
    String getTableId();
    int getPartySize();
    TimeSlot getSlot();
    Status getStatus();
}
