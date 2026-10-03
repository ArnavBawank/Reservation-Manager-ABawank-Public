import java.io.*;
import java.util.*;

/**
 * file utility class
 *
 * helper methods for reading and writing lines to files
 * used by file based stores for file operations
 *
 * @author Rocco Falco
 * @version Nov 10th, 2025
 */
public class FileUtil implements FileUtilInterface {
    public static List<String> readLines(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null)
                lines.add(line.trim());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }
    public static void writeLines(File file, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines)
                bw.write(line + "\n");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public void writeAtomic(String path, List<String> lines) throws IOException {
        File target = new File(path);
        File temp = new File(path + ".tmp");

        // write to temp file first
        writeLines(temp, lines); // can still call static method

        // rename temp -> target (atomic-ish)
        if (!temp.renameTo(target)) {
            throw new IOException("Failed to rename temp file: " + path);
        }
    }
}
