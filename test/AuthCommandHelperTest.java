import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit test class for AuthCommandHelper.
 * Verifies the functionality of the command formatting and splitting utility methods.
 * @author Daniel Gong (gong256)
 * @version 2025-11-10
 */
public class AuthCommandHelperTest {

    /**
     * Verifies that formatCreateUser correctly concatenates the command type,
     * email, and password into a single space-separated string.
     */
    @Test
    public void testFormatCreateUser() {
        String result = AuthCommandHelper.formatCreateUser("alice@example.com", "pass");
        assertEquals("CREATE_USER alice@example.com pass", result);
    }

    /**
     * Verifies that formatLogin correctly concatenates the command type,
     * email, and password into a single space-separated string.
     */
    @Test
    public void testFormatLogin() {
        String result = AuthCommandHelper.formatLogin("bob@example.com", "123");
        assertEquals("LOGIN bob@example.com 123", result);
    }

    /**
     * Tests splitCommand with a standard, single-spaced command string.
     */
    @Test
    public void testSplitCommandSimple() {
        String[] tokens = AuthCommandHelper.splitCommand("LOGIN alice p");
        assertArrayEquals(new String[] { "LOGIN", "alice", "p" }, tokens);
    }

    /**
     * Tests splitCommand with leading, trailing, and irregular internal whitespace.
     */
    @Test
    public void testSplitCommandTrimWhitespace() {
        String[] tokens = AuthCommandHelper.splitCommand("  LOGIN   alice   p   ");
        assertArrayEquals(new String[] { "LOGIN", "alice", "p" }, tokens);
    }

    /**
     * Tests splitCommand behavior when an empty string is provided.
     */
    @Test
    public void testSplitCommandEmpty() {
        String[] tokens = AuthCommandHelper.splitCommand("");
        assertEquals(1, tokens.length); // split on empty returns [""]
    }

    /**
     * Tests splitCommand with a null input.
     */
    @Test
    public void testSplitCommandNull() {
        String[] tokens = AuthCommandHelper.splitCommand(null);
        assertEquals(0, tokens.length);
    }
}
