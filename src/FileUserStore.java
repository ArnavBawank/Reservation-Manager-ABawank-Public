import java.io.*;
/**
* Represents a user store that persists users to a file.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/
public class FileUserStore implements UserStore {
    private final Object lock = new Object();
    private final String filePath;

    /**
     * Constructs a FileUserStore that reads and writes users to the given file path.
     * If the file path is null or empty, defaults to "users.txt".
     * The constructor ensures that the file exists.
     *
     * @param filePath the file path for storing users
     */
    public FileUserStore(String filePath) {
        this.filePath = filePath == null || filePath.isEmpty() ? "users.txt" : filePath;
        // ensure file exists
        try {
            File f = new File(this.filePath);
            if (!f.exists()) f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a {@link User} to the file by appending it as a new line.
     *
     * @param user the user to save
     */
    public void save(User user) {
        synchronized (lock) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
                pw.println(user.getId() + "," + user.getEmail() + "," + user.getPassword());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Finds and returns a {@link User} by their email address.
     *
     * @param email the email of the user to find
     * @return the User object if found, or null if not found
     */
    public User findByEmail(String email) {
        File file = new File(filePath);
        if (!file.exists()) return null;
        try (BufferedReader bfr = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[1].equals(email)) {
                    int id = Integer.parseInt(parts[0]);
                    return new User(id, parts[1], parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes the user with the specified ID.
     * The deletion is done atomically by writing all other users to a temporary file
     * and then replacing the original file.
     *
     * @param userId the ID of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    public boolean deleteById(int userId) {
        File inputFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");
        if (!inputFile.exists()) {
            return false;
        }
        synchronized (lock) {
            try (BufferedReader bfr = new BufferedReader(new FileReader(inputFile));
                 PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = bfr.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 1) {
                        try {
                            int id = Integer.parseInt(parts[0]);
                            if (id != userId) {
                                pw.println(line);
                            }
                        } catch (NumberFormatException nfe) {
                            // keep malformed lines
                            pw.println(line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!inputFile.delete()) {
                System.out.println("Could not delete original file");
                return false;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename temp file");
                return false;
            }
            return true;
        }
    }

}