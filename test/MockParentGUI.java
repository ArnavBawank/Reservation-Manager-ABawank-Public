/**
* @author Daniel Gong (gong256)
* @version 2025-12-03
* Mock implementation of the ReservationGUI for testing purposes.
*/
public class MockParentGUI extends ReservationGUI {

    public String lastShownPanel = null;
    public boolean logoutCalled = false;

    /**
     * Constructs a new MockParentGUI with the given mock client.
     *
     * @param client the mock reservation client to use
     */
    public MockParentGUI(MockReservationClient client) {
        super("localhost", 8888, client);
    }

    /**
     * Records the panel name requested to be shown.
     *
     * @param name the name of the panel
     */
    @Override
    public void showPanel(String name) {
        lastShownPanel = name;
    }
    /**
     * Records that logout was called.
     */
    public void logout() {
        logoutCalled = true;
    }
}
