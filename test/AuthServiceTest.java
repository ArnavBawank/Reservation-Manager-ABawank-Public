import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.File;
/**
* Unit tests for the AuthServiceSimple class.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/

public class AuthServiceTest {

    private AuthServiceSimple authService;
    private FileUserStore userStore;
    private SessionMap sessionService;

    private static final String TEST_FILE = "test_users.txt";

    @Before
    public void setUp() {
        // Delete test file if it exists
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();

        userStore = new FileUserStore(TEST_FILE);
        sessionService = new SessionMap();
        authService = new AuthServiceSimple(userStore, sessionService);
    }

    @After
    public void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
    }

    @Test
    public void testCreateUser() {
        int userId = authService.createUser("alice@example.com", "password123");
        assertTrue(userId >= 10000000 && userId <= 99999999);

        User user = userStore.findByEmail("alice@example.com");
        assertNotNull(user);
        assertEquals("alice@example.com", user.getEmail());
    }

    @Test
    public void testLogin() {
        int userId = authService.createUser("bob@example.com", "secretpass");
        int sessionId = authService.login("bob@example.com", "secretpass");
        assertTrue(sessionId >= 10000000 && sessionId <= 99999999);

        int mappedUserId = Integer.parseInt(sessionService.getUserId(String.valueOf(sessionId)));
        assertEquals(userId, mappedUserId);
    }

    @Test
    public void testDeleteUser() {
        authService.createUser("charlie@example.com", "mypassword");
        int sessionId = authService.login("charlie@example.com", "mypassword");

        authService.deleteUser(String.valueOf(sessionId));

        assertNull(userStore.findByEmail("charlie@example.com"));
        assertNull(sessionService.getUserId(String.valueOf(sessionId)));
    }

    @Test
    public void testBadCredentials() {
        authService.createUser("dave@example.com", "pass123");

        int sessionId = authService.login("dave@example.com", "wrongpass");
        assertEquals(-1, sessionId);
    }

    @Test
    public void testPersistence() {
        authService.createUser("eve@example.com", "securepass");

        // Restart FileUserStore
        userStore = new FileUserStore(TEST_FILE);
        authService = new AuthServiceSimple(userStore, sessionService);

        User user = userStore.findByEmail("eve@example.com");
        assertNotNull(user);

        int sessionId = authService.login("eve@example.com", "securepass");
        assertTrue(sessionId >= 10000000 && sessionId <= 99999999);
    }

    @Test
    public void testDuplicateEmailRegistration() {
        // First registration should succeed
        int firstUserId = authService.createUser("frank@example.com", "password123");
        assertTrue(firstUserId >= 10000000 && firstUserId <= 99999999);

        // Attempt to register with same email should fail
        int secondUserId = authService.createUser("frank@example.com", "differentpass");
        assertEquals(-1, secondUserId);

        // Verify only first user exists and can login
        User user = userStore.findByEmail("frank@example.com");
        assertNotNull(user);
        assertEquals("password123", user.getPassword());

        int sessionId = authService.login("frank@example.com", "password123");
        assertTrue(sessionId >= 10000000 && sessionId <= 99999999);
    }
}
