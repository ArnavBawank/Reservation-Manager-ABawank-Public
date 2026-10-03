import org.junit.*;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

/**
 * seating store persistence test
 *
 * tests save and reload, updates, deletions
 * verifies data persists correctly to file
 *
 * @author Rocco Falco
 * @version Nov 10th, 2025
 */
public class SeatingStorePersistenceTest {

    private static final String SEATING_FILE = "data/seating_test.txt";

    @Before
    public void cleanFile() {
        File f = new File(SEATING_FILE);
        if (f.exists()) f.delete();
    }

    @Test
    public void saveThenReloadContainsTable() {
        FileSeatingStore store1 = new FileSeatingStore(SEATING_FILE);
        store1.upsert(new Table("T1", 4, "Main"));
        store1.flush();

        FileSeatingStore store2 = new FileSeatingStore(SEATING_FILE);
        Optional<Table> got = store2.get("T1");
        
        assertTrue(got.isPresent());
        assertEquals("T1", got.get().getId());
        assertEquals(4, got.get().getCapacity());
        assertEquals("Main", got.get().getSection());
    }

    @Test
    public void upsertSameIdReplacesTable() {
        FileSeatingStore store = new FileSeatingStore(SEATING_FILE);
        
        store.upsert(new Table("T2", 4, "Main"));
        store.upsert(new Table("T2", 6, "Private")); // same id, updated
        store.flush();

        FileSeatingStore reload = new FileSeatingStore(SEATING_FILE);
        Optional<Table> got = reload.get("T2");
        assertTrue(got.isPresent());
        assertEquals(6, got.get().getCapacity());
        assertEquals("Private", got.get().getSection());
    }

    @Test
    public void removeThenReloadIsGone() {
        FileSeatingStore store1 = new FileSeatingStore(SEATING_FILE);
        store1.upsert(new Table("T3", 4, "Main"));
        store1.flush();

        assertTrue(store1.remove("T3"));
        store1.flush();

        FileSeatingStore store2 = new FileSeatingStore(SEATING_FILE);
        assertFalse(store2.get("T3").isPresent());
    }

    @Test
    public void getMissingTableReturnsEmpty() {
        FileSeatingStore store = new FileSeatingStore(SEATING_FILE);
        assertFalse(store.get("nonexistent").isPresent());
    }

    @Test
    public void removeMissingTableReturnsFalse() {
        FileSeatingStore store = new FileSeatingStore(SEATING_FILE);
        assertFalse(store.remove("nonexistent"));
    }
}

