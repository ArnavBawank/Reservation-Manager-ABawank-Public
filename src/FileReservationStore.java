import java.util.*;
import java.io.*;
import java.time.*;

/**
 * file based reservation store implementation
 *
 * saves reservations to data/reservations.txt in csv format
 * thread safe using synchronized blocks
 * filters listBySlot to only booked reservations
 *
 * @author Krishna Vijay
 * @version Nov 10th, 2025
 */
public class FileReservationStore implements ReservationStore {
    private static final Object LOCK = new Object();
    private static final String FILE_NAME = "data/reservations.txt";
    private static final String DATA_DIR = "data";



    private final List<Reservation> reservations = new ArrayList<>();
    File reservationsFile = new File(FILE_NAME);
    File dataDir = new File(DATA_DIR);

    //loads reservations from file into memory via helper methods
    public FileReservationStore() {
        synchronized (LOCK) {
            ensureDataFile();
            loadFromFile();
        }
    }
    // makes sure dir and file exists for reservation. creates if not exists.
    private void ensureDataFile() {
        if (dataDir.exists() && dataDir.isDirectory()) {
        } else {
            if (!dataDir.mkdir()) {
                throw new RuntimeException("Failed to create data directory");
            }
        }
        if (reservationsFile.exists() && !reservationsFile.isDirectory()) {
            return;
        } else {
            try {
                reservationsFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create data file", e);
            }
        }
    }

    // loads reservations from file into memory, one line per reservation
    private void loadFromFile() {
        reservations.clear();
        
        try (BufferedReader br = new BufferedReader(new FileReader(reservationsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",", -1);
                if (parts.length != 7) {
                    continue;
                }
                
                String id = parts[0].trim();
                String userId = parts[1].trim();
                String tableId = parts[2].trim();
                
                if (userId.isEmpty() || tableId.isEmpty()) {
                    continue;
                }
                
                int partySize;
                try {
                    partySize = Integer.parseInt(parts[3].trim());
                    if (partySize <= 0) {
                        continue;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
                
                LocalDate date;
                LocalTime time;
                try {
                    date = LocalDate.parse(parts[4].trim());
                    time = LocalTime.parse(parts[5].trim());
                } catch (Exception e) {
                    continue;
                }
                time = time.withSecond(0).withNano(0);
                
                Status status;
                try {
                    status = Status.valueOf(parts[6].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    continue;
                }
                
                TimeSlot slot = new TimeSlot(date, time);
                Reservation res = new Reservation(id, userId, tableId, partySize, slot, status);
                
                int idx = indexOfId(id);
                if (idx >= 0) {
                    reservations.set(idx, res);
                } else {
                    reservations.add(res);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to load reservations file", e);
        }
    }
    
    // finds the index of a reservation with the given id, returns negative one if not found
    private int indexOfId(String id) {
        for (int i = 0; i < reservations.size(); i++) {
            if (id.equals(reservations.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }
            
    

    // write the whole list back to data/reservations.txt
    private void persistAll() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reservationsFile, /*append=*/false))) {
            for (Reservation r : reservations) {
                bw.write(toCsv(r));   // one line per reservation
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException("failed to write reservations file", e);
        }
    }

    // format: id,userId,tableId,partySize,YYYY MM DD,HH:mm,STATUS
    private String toCsv(Reservation r) {
        LocalDate d = r.getSlot().getDate();
        LocalTime t = r.getSlot().getStart().withSecond(0).withNano(0);
        String hhmm = String.format("%02d:%02d", t.getHour(), t.getMinute());
        return r.getId() + "," +
            (r.getUserId() == null ? "" : r.getUserId()) + "," +
            (r.getTableId() == null ? "" : r.getTableId()) + "," +
            r.getPartySize() + "," +
            d + "," +
            hhmm + "," +
            r.getStatus().name();
    }

    @Override
    public synchronized Reservation save(Reservation r) {
        // ensure only one thread mutates + writes at a time
        synchronized (LOCK) {
            int idx = indexOfId(r.getId());
            if (idx >= 0) {
                // update existing reservation with same id
                reservations.set(idx, r);
            } else {
                // new reservation, append to list
                reservations.add(r);
            }
            // persist entire list back to disk
            persistAll();
            return r;
        }
    }

    @Override
    public synchronized Optional<Reservation> get(String id) {
        // read under lock to avoid seeing a half updated list
        synchronized (LOCK) {
            for (Reservation r : reservations) {
                if (id != null && id.equals(r.getId())) {
                    return Optional.of(r);
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public synchronized List<Reservation> listBySlot(TimeSlot slot) {
        // collect only BOOKED reservations that match the same date+time
        synchronized (LOCK) {
            List<Reservation> out = new ArrayList<>();
            for (Reservation r : reservations) {
                if (r.getStatus() == Status.BOOKED) {
                    TimeSlot s = r.getSlot();
                    if (s.getDate().equals(slot.getDate()) &&
                        s.getStart().equals(slot.getStart())) {
                        out.add(r);
                    }
                }
            }
            return out;
        }
    }

    @Override
    public synchronized boolean delete(String id) {
        // remove by id, then persist if something was removed
        synchronized (LOCK) {
            int idx = indexOfId(id);
            if (idx >= 0) {
                reservations.remove(idx);
                persistAll();
                return true;
            }
            return false;
        }
    }

    @Override
    public synchronized void flush() {
        // force rewrite of the file from current in memory list
        synchronized (LOCK) {
            persistAll();
        }
    }
}
