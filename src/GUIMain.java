/**
* @author Daniel Gong (gong256)
* @version 2025-12-03
* This class initializes the graphical user interface by creating an instance
*/
public class GUIMain {
    /**
     * The main method that starts the Reservation GUI application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            ReservationGUI gui = new ReservationGUI();
            gui.setVisible(true);
        } catch (Exception e) {
            System.out.println("Startup error: " + e.getMessage());
        }
    }
}

