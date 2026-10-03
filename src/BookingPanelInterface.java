/**
 * booking panel interface
 *
 * defines behaviors for the booking panel component
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public interface BookingPanelInterface {
    // refreshes the panel display
    void refresh();

    // sets status message for user feedback
    void setStatus(String message);

    // clears the table list display
    void clearTableList();

    // parses server response into table display strings
    String[] parseTableResponse(String response);

    // parses server response into reservation display strings
    String[] parseReservationResponse(String response);
}
