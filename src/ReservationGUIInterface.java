/**
* @author Daniel Gong (gong256)
* @version 2025-12-03
* Interface defining the core functionalities of the Reservation GUI.
*/
public interface ReservationGUIInterface {
    void showPanel(String name);
    void setLoggedIn(int sessionId, String email);
    void logout();
    ReservationClient getClient();
    String getCurrentEmail();
    boolean isLoggedIn();
}
