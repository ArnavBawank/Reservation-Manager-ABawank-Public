import java.util.*;

/**
 * availability service interface
 *
 * lists open tables for a time slot, sorted by capacity then id
 * filters tables by party size capacity
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public interface AvailabilityService {
    List<Table> listOpen(TimeSlot slot); // list open tables by slot and capacity

    // list tables that can fit party size
    List<Table> listOpenForParty(TimeSlot slot, int partySize);

}