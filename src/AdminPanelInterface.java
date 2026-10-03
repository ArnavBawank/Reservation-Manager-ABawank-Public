/**
 * AdminPanelInterface.java
 * This is the interface for the admin GUI class.
 *
 * @author Arnav Bawankule
 * @version 1.0
 */

public interface AdminPanelInterface {
    void refresh();

    //shows status message to user.
    void setStatus(String message);
}