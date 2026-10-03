import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * my reservations service test
 *
 * ensures filtering by user id works
 *
 * @author Krishna Vijay
 * @version Nov 24th, 2025
 */
public class MyReservationsServiceTest {

    private static final String PATH = "data/reservations_my_test.txt";

    @Before
    public void setUp() throws Exception {
        File f = new File(PATH);
        if (f.exists()) f.delete();
        f.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write("r1,u1,T1,2,2025-12-01,18:00,BOOKED\n");
            bw.write("r2,u2,T2,4,2025-12-02,19:00,BOOKED\n");
            bw.write("bad,line,here\n");
            bw.write("r3,u1,T3,3,2025-12-03,20:00,CANCELLED\n");
        }
    }

    @After
    public void clean() {
        File f = new File(PATH);
        if (f.exists()) f.delete();
    }

    @Test
    public void filtersByUser() {
        MyReservationsService svc = new MyReservationsService(PATH);
        List<Reservation> res = svc.listForUser("u1");
        assertEquals(2, res.size());
        assertEquals("r1", res.get(0).getId());
        assertEquals("r3", res.get(1).getId());
    }
}
