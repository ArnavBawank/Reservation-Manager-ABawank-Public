/**
 * reservation model
 *
 * represents booking with id, user id, table id, party size, time slot, and status
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class Reservation implements ReservationInterface {
    private String id;
    private String userId;
    private String tableId;
    private int partySize;
    private TimeSlot slot;
    private Status status;

    public Reservation(String id, String userId, String tableId, int partySize, TimeSlot slot, Status status) {
        this.id = id;
        this.userId = userId;
        this.tableId = tableId;
        this.partySize = partySize; 
        this.slot = slot;
        this.status = status;
    }   
    public String getId() { 
        return id; 
    }
    public String getUserId() { 
        return userId; 
    }
    public String getTableId() { 
        return tableId; 
    }
    public int getPartySize() { 
        return partySize; 
    }
    public TimeSlot getSlot() { 
        return slot; 
    }
    public Status getStatus() { 
        return status; 
    }
}