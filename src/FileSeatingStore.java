import java.io.*;
import java.util.*;

/**
 * file based seating store implementation
 *
 * saves tables to data/seating.txt in csv format
 * thread safe using synchronized blocks
 * loads default tables if file doesn't exist
 *
 * @author Rocco Falco
 * @version Nov 10th, 2025
 */
public class FileSeatingStore implements SeatingStore {
    private final File file;
    private final List<Table> tables = new ArrayList<>();
    
    public FileSeatingStore(String filename) {
        this.file = new File(filename);
        load();
    }
    
    private int indexOfId(String tableId) {
        for (int i = 0; i < tables.size(); i++) {
            if (tableId.equals(tables.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public synchronized List<Table> listAll() {
        return new ArrayList<>(tables);
    }
    
    @Override
    public synchronized Optional<Table> get(String tableId) {
        int idx = indexOfId(tableId);
        if (idx >= 0) {
            return Optional.of(tables.get(idx));
        }
        return Optional.empty();
    }
    
    @Override
    public synchronized void upsert(Table table) {
        int idx = indexOfId(table.getId());
        if (idx >= 0) {
            tables.set(idx, table);
        } else {
            tables.add(table);
        }
        save();
    }
    
    @Override
    public synchronized boolean remove(String tableId) {
        int idx = indexOfId(tableId);
        if (idx >= 0) {
            tables.remove(idx);
            save();
            return true;
        }
        return false;
    }
    
    @Override
    public synchronized void flush() {
        save();
    }
    
    public synchronized List<String> getAvailableSeats() {
        List<String> result = new ArrayList<>();
        for (Table t : tables) {
            result.add(t.getId());
        }
        return result;
    }
    public synchronized boolean reserveSeat(String seat) {
        int idx = indexOfId(seat);
        if (idx >= 0) {
            tables.remove(idx);
            save();
            return true;
        }
        return false;
    }
    public synchronized boolean cancelSeat(String seat) {
        int idx = indexOfId(seat);
        if (idx < 0) {
            tables.add(new Table(seat, 4, "Main"));
            save();
            return true;
        }
        return false;
    }
    private synchronized void save() {
        List<String> lines = new ArrayList<>();
        for (Table t : tables) {
            lines.add(t.getId() + "," + t.getCapacity() + "," + t.getSection());
        }
        FileUtil.writeLines(file, lines);
    }
    private synchronized void load() {
        tables.clear();
        if (file.exists()) {
            List<String> lines = FileUtil.readLines(file);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        String id = parts[0].trim();
                        int capacity = Integer.parseInt(parts[1].trim());
                        String section = parts[2].trim();
                        tables.add(new Table(id, capacity, section));
                    } catch (NumberFormatException e) {
                        tables.add(new Table(line.trim(), 4, "Main"));
                    }
                } else {
                    tables.add(new Table(line.trim(), 4, "Main"));
                }
            }
        } else {
            tables.add(new Table("Table1", 4, "Main"));
            tables.add(new Table("Table2", 4, "Main"));
            tables.add(new Table("Table3", 4, "Main"));
            tables.add(new Table("PartyRoom", 8, "Main"));
        }
    }
}

