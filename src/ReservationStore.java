import java.util.*;

/**
 * reservation store interface
 *
 * saves, retrieves, and deletes reservations
 * lists reservations by time slot (only booked status)
 * persists to file storage
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public interface ReservationStore {
    Reservation save(Reservation r);
    
    Optional<Reservation> get(String id);
    
    List<Reservation> listBySlot(TimeSlot slot);
    
    boolean delete(String id);
    
    void flush();
}