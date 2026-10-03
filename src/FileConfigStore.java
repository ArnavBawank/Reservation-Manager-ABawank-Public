import java.time.*;
import java.util.*;
import java.io.*;

/**
 * file based config store implementation
 *
 * saves configuration to data/config.txt in csv format
 * thread safe using synchronized blocks
 * loads hours and section locks from file
 *
 * @author Arnav Bawank
 * @version Nov 10th, 2025
 */
public class FileConfigStore implements ConfigStore {

    private static final Object LOCK = new Object();
    private static final String PATH = "data/config.txt";

    private final FileUtil fileUtil;

    public FileConfigStore(FileUtil fileUtil) {
        this.fileUtil = fileUtil;

        // initialize the config file if missing
        File f = new File(PATH);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs(); // create parent folder if needed
                fileUtil.writeAtomic(PATH, new ArrayList<>()); // empty file
            } catch (Exception e) {
                System.out.println("Failed to initialize config file: " + e.getMessage());
            }
        }
    }

    @Override
    public Optional<Config> load() {
        synchronized (LOCK) {
            try {
                List<String> lines = FileUtil.readLines(new File(PATH));
                Map<DayOfWeek, Hours> hoursMap = new EnumMap<>(DayOfWeek.class);
                List<SectionLock> locks = new ArrayList<>();

                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 0) continue;

                    if (parts[0].equalsIgnoreCase("HOURS")) {
                        DayOfWeek day = DayOfWeek.valueOf(parts[1]);
                        LocalTime open = LocalTime.parse(parts[2]);
                        LocalTime close = LocalTime.parse(parts[3]);
                        hoursMap.put(day, new Hours(open, close));
                    } else if (parts[0].equalsIgnoreCase("LOCK")) {
                        LocalDate date = LocalDate.parse(parts[1]);
                        LocalTime time = LocalTime.parse(parts[2]);
                        String section = parts[3];
                        locks.add(new SectionLock(new TimeSlot(date, time), section));
                    }
                }

                return Optional.of(new Config(hoursMap, locks));

            } catch (Exception e) {
                System.out.println("Error reading configuration: " + e.getMessage());
                return Optional.empty();
            }
        }
    }

    @Override
    public void save(Config cfg) {
        synchronized (LOCK) {
            try {
                List<String> lines = new ArrayList<>();

                // write hours
                for (Map.Entry<DayOfWeek, Hours> entry : cfg.getHoursByDay().entrySet()) {
                    Hours h = entry.getValue();
                    lines.add("HOURS," + entry.getKey() + "," + h.getOpen() + "," + h.getClose());
                }

                // write locks
                for (SectionLock lock : cfg.getLocks()) {
                    lines.add("LOCK," + lock.getSlot().getDate() + "," +
                                      lock.getSlot().getStart() + "," +
                                      lock.getSection());
                }

                // use fileUtil instance to write atomically
                fileUtil.writeAtomic(PATH, lines);

            } catch (Exception e) {
                System.out.println("Error writing config: " + e.getMessage());
            }
        }
    }
}
