import java.util.Scanner;

/**
 * The main program for the client side of the reservation system.
 * <p>
 * This class handles user input, connects to the server, and displays
 * the menus that allow users to make or cancel reservations.
 * </p>
 * @author Daniel Gong (gong256)
 * @version 2025-11-10
 */
public class ClientMain {

    /**
     * The starting point of the application.
     * <p>
     * It asks the user for the server's address and port, connects to the server,
     * and runs the main loop where the user can select different menu options.
     * </p>
     * @param args command line arguments (not used in this program)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReservationClient client = new ReservationClient();

        try {
            System.out.print("Enter server host: ");
            String host = scanner.nextLine().trim();

            System.out.print("Enter server port: ");
            int port = Integer.parseInt(scanner.nextLine().trim());

            client.connect(host, port);
            System.out.println("Connected to server.\n");

            boolean running = true;

            while (running) {
                printMainMenu();
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        handleCreateAccount(scanner, client);
                        break;
                    case "2":
                        handleLogin(scanner, client);
                        break;
                    case "3":
                        handleLogout(client);
                        break;
                    case "4":
                        handleListOpen(scanner, client);
                        break;
                    case "5":
                        handleBook(scanner, client);
                        break;
                    case "6":
                        handleCancel(scanner, client);
                        break;
                    case "7":
                        handleListMyReservations(client);
                        break;
                    case "8":
                        handleAdminMenu(scanner, client);
                        break;
                    case "9":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option.\n");
                }
            }

            client.disconnect();
            System.out.println("Disconnected. Goodbye!");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Displays the main list of options available to the user.
     */
    private static void printMainMenu() {
        System.out.println("========== MAIN MENU ==========");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. Logout");
        System.out.println("4. List Open Slots");
        System.out.println("5. Book Reservation");
        System.out.println("6. Cancel Reservation");
        System.out.println("7. List My Reservations");
        System.out.println("8. Admin Menu (optional)");
        System.out.println("9. Quit");
        System.out.println("================================");
    }

    /**
     * Displays the options specifically for the admin sub-menu.
     */
    private static void printAdminMenu() {
        System.out.println("========== ADMIN MENU ==========");
        System.out.println("1. (Placeholder) Admin action");
        System.out.println("2. Return to Main Menu");
        System.out.println("================================");
    }

    /**
     * Asks the user for an email and password, then tries to create a new account.
     * * @param scanner used to read the user's input
     * @param client  used to send the create command to the server
     */
    private static void handleCreateAccount(Scanner scanner, ReservationClient client) {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String result = client.createAccount(email, password);
        System.out.println(result + "\n");
    }

    /**
     * Asks for login credentials and attempts to log the user in.
     * * @param scanner used to read the user's input
     * @param client  used to send the login command to the server
     */
    private static void handleLogin(Scanner scanner, ReservationClient client) {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String result = client.login(email, password);
        System.out.println(result + "\n");
    }

    /**
     * Logs the current user out of the system.
     * * @param client used to send the logout command to the server
     */
    private static void handleLogout(ReservationClient client) {
        String result = client.logout();
        System.out.println(result + "\n");
    }

    /**
     * Asks for a date and party size, then shows available time slots.
     * * @param scanner used to read the date and size
     * @param client  used to request the list from the server
     */
    private static void handleListOpen(Scanner scanner, ReservationClient client) {
        System.out.print("Enter date/time (yyyy-MM-dd HH:mm): ");
        String dt = scanner.nextLine().trim();

        System.out.print("Party size: ");
        int size = Integer.parseInt(scanner.nextLine().trim());

        String result = client.listOpen(dt, size);
        System.out.println(result + "\n");
    }

    /**
     * Asks the user for details and tries to book a new reservation.
     * * @param scanner used to read the date and party size
     * @param client  used to send the booking request to the server
     */
    private static void handleBook(Scanner scanner, ReservationClient client) {
        System.out.print("Enter date/time (yyyy-MM-dd HH:mm): ");
        String dt = scanner.nextLine().trim();

        System.out.print("Party size: ");
        int size = Integer.parseInt(scanner.nextLine().trim());

        String result = client.book(dt, size);
        System.out.println(result + "\n");
    }

    /**
     * Asks for a reservation ID and attempts to cancel that reservation.
     * * @param scanner used to read the reservation ID
     * @param client  used to send the cancel request to the server
     */
    private static void handleCancel(Scanner scanner, ReservationClient client) {
        System.out.print("Reservation ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        String result = client.cancel(id);
        System.out.println(result + "\n");
    }

    /**
     * Displays a list of all reservations made by the currently logged-in user.
     * * @param client used to request the history from the server
     */
    private static void handleListMyReservations(ReservationClient client) {
        String result = client.listMyReservations();
        System.out.println(result + "\n");
    }

    /**
     * Opens a sub-menu for administrative tasks.
     * <p>
     * This menu runs in its own loop until the user chooses to return 
     * to the main menu.
     * </p>
     * * @param scanner used to read menu choices
     * @param client  used for any future admin commands
     */
    private static void handleAdminMenu(Scanner scanner, ReservationClient client) {
        boolean adminRunning = true;

        while (adminRunning) {
            printAdminMenu();
            System.out.print("Choose an admin option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Admin features not implemented.\n");
                    break;
                case "2":
                    adminRunning = false;
                    break;
                default:
                    System.out.println("Invalid option.\n");
            }
        }
    }
}
