import java.util.List;

/**
 * my reservations service interface
 *
 * filters reservations by user id
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public interface MyReservationsServiceInterface {
    List<Reservation> listForUser(String userId);
}
