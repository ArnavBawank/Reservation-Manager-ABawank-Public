import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * tests for file util
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class FileUtilTest {

    private File testFile;

    @Before
    public void setUp() {
        testFile = new File("data/test_fileutil.txt");
    }

    @After
    public void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
        File temp = new File("data/test_fileutil.txt.tmp");
        if (temp.exists()) {
            temp.delete();
        }
    }

    @Test
    public void testWriteAndReadLines() {
        List<String> lines = new ArrayList<String>();
        lines.add("line1");
        lines.add("line2");
        lines.add("line3");

        FileUtil.writeLines(testFile, lines);
        List<String> read = FileUtil.readLines(testFile);

        assertEquals(3, read.size());
        assertEquals("line1", read.get(0));
        assertEquals("line2", read.get(1));
        assertEquals("line3", read.get(2));
    }

    @Test
    public void testReadNonExistentFile() {
        File fake = new File("data/nonexistent_file_xyz.txt");
        List<String> lines = FileUtil.readLines(fake);
        assertTrue(lines.isEmpty());
    }

    @Test
    public void testWriteEmptyList() {
        List<String> empty = new ArrayList<String>();
        FileUtil.writeLines(testFile, empty);
        List<String> read = FileUtil.readLines(testFile);
        assertTrue(read.isEmpty());
    }

    @Test
    public void testImplementsInterface() {
        FileUtil util = new FileUtil();
        assertTrue(util instanceof FileUtilInterface);
    }
}
