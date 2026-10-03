import org.junit.Test;
import static org.junit.Assert.*;

/**
 * tests for user model
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class UserTest {

    @Test
    public void testConstructorAndGetters() {
        User user = new User(1, "test@email.com", "password123");
        assertEquals(1, user.getId());
        assertEquals("test@email.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testDifferentUsers() {
        User user1 = new User(1, "user1@test.com", "pass1");
        User user2 = new User(2, "user2@test.com", "pass2");

        assertNotEquals(user1.getId(), user2.getId());
        assertNotEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    public void testEmptyStrings() {
        User user = new User(0, "", "");
        assertEquals(0, user.getId());
        assertEquals("", user.getEmail());
        assertEquals("", user.getPassword());
    }

    @Test
    public void testImplementsInterface() {
        User user = new User(1, "test@email.com", "pass");
        assertTrue(user instanceof UserInterface);
    }
}
