import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * my reservations service
 *
 * reads reservation records and filters by user id
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public class MyReservationsService implements MyReservationsServiceInterface {

    private final String path;
    private final Object lock = new Object();

    public MyReservationsService(String path) {
        this.path = path;
    }

    public MyReservationsService() {
        this("data/reservations.txt");
    }

    public List<Reservation> listForUser(String userId) {
        synchronized (lock) {
            List<Reservation> out = new ArrayList<>();
            File f = new File(path);
            if (!f.exists()) {
                return out;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",", -1);
                    if (p.length != 7) continue;
                    String uid = p[1].trim();
                    if (!uid.equals(userId)) continue;
                    String id = p[0].trim();
                    String tableId = p[2].trim();
                    int n;
                    try {
                        n = Integer.parseInt(p[3].trim());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    LocalDate d;
                    LocalTime t;
                    try {
                        d = LocalDate.parse(p[4].trim());
                        t = LocalTime.parse(p[5].trim()).withSecond(0).withNano(0);
                    } catch (Exception e) {
                        continue;
                    }
                    Status st;
                    try {
                        st = Status.valueOf(p[6].trim());
                    } catch (Exception e) {
                        continue;
                    }
                    out.add(new Reservation(id, uid, tableId, n, new TimeSlot(d, t), st));
                }
            } catch (IOException e) {
                // ignore errors for now
            }
            return out;
        }
    }
}
