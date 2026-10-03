/**
 * my reservations panel interface
 *
 * defines behaviors for the my reservations panel component
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public interface MyReservationsPanelInterface {
    // refreshes the reservation list from server
    void refresh();

    // sets status message for user feedback
    void setStatus(String message);

    // parses server response into reservation display strings
    String[] parseReservationResponse(String response);

    // gets the selected reservation id from the list
    String getSelectedReservationId();
}
