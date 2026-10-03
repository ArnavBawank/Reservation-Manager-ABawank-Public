/**
 * table model
 *
 * represents restaurant table with id, capacity, and section name
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class Table implements TableInterface {
    private String id;
    private int capacity;
    private String section;

    public Table(String id, int capacity, String section) {
        this.id = id;
        this.capacity = capacity;
        this.section = section;
    }

    public String getId() { 
        return id; 
    }
    public int getCapacity() { 
        return capacity; 
    }
    public String getSection() { 
        return section; 
    }
}
