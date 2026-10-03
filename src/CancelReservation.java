import java.io.*;
import java.util.*;

/**
 * cancel reservation demo
 *
 * simple demo class that allows users to cancel reservations by name
 *
 * @author Rocco Falco
 * @version Nov 10th, 2025
 */
public class CancelReservation {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            File file = new File("reservations.txt");

            if (!file.exists()) {
                System.out.println("No reservations found.");
                return;
            }

            List<String> reservations = new ArrayList<>();
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    reservations.add(reader.nextLine());
                }
            } catch (IOException e) {
                System.out.println("Error reading file.");
                return;
            }

            System.out.print("Enter your name to cancel reservation: ");
            String name = sc.nextLine().trim();

            boolean canceled = false;
            Iterator<String> it = reservations.iterator();
            while (it.hasNext()) {
                String line = it.next();
                if (line.toLowerCase().startsWith(name.toLowerCase() + ",")) {
                    it.remove();
                    canceled = true;
                }
            }

            if (canceled) {
                try (FileWriter writer = new FileWriter(file, false)) {
                    for (String r : reservations) writer.write(r + "\n");
                } catch (IOException e) {
                    System.out.println("Error updating file.");
                }
                System.out.println("Reservation canceled for " + name);
            } else {
                System.out.println("No reservation found for that name.");
            }
        }
    }
}
