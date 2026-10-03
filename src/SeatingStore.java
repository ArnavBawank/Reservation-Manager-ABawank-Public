import java.util.*;

/**
 * seating store interface
 *
 * manages restaurant tables (add, remove, update, list)
 * retrieves tables by id
 *
 * @author Rocco Falco
 * @version Nov 10th, 2025
 */
public interface SeatingStore {
    // returns a copy of all tables currently known
    List<Table> listAll();
    Optional<Table> get(String tableId);
    // adds or replaces a table by id
    void upsert(Table table);
    boolean remove(String tableId);
    void flush();
}