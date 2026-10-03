import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * file utility interface
 *
 * defines file reading and writing operations
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public interface FileUtilInterface {
    void writeAtomic(String path, List<String> lines) throws IOException;
}
