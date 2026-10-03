import java.util.Random;
/**
* Simple implementation of the AuthService interface.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/

public class AuthServiceSimple implements AuthService {
    private final UserStore userStore;
    private final SessionService sessionService;
    private final Object lock = new Object();
    private final Random random = new Random();

     /**
     * Constructs an AuthServiceSimple with the given user store and session service.
     *
     * @param userStore the user store used for persisting and retrieving users
     * @param sessionService the session service used for creating and managing sessions
     */
    public AuthServiceSimple(UserStore userStore, SessionService sessionService) {
        this.userStore = userStore;
        this.sessionService = sessionService;
    }

    /**
     * Generates a random 8-digit user ID.
     *
     * @return a randomly generated 8-digit integer
     */
    private int generateId() {
        return 10_000_000 + random.nextInt(90_000_000);
    }

    /**
     * Creates a new user with the specified email and password.
     * Returns the user ID if successful, or -1 if the email is already in use.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return the new user's ID, or -1 if creation failed
     */
    public int createUser(String email, String password) {
        synchronized (lock) {
            if (userStore.findByEmail(email) != null) {
                return -1;
            }
            int id = generateId();
            User user = new User(id, email, password);
            userStore.save(user);
            return id;
        }
    }

    /**
     * Attempts to log in a user with the given email and password.
     * If successful, returns the session ID as an integer. Returns -1 on failure.
     *
     * @param email the user's email
     * @param password the user's password
     * @return the session ID as an integer, or -1 if login failed
     */
    public int login(String email, String password) {
        User user = userStore.findByEmail(email);
        if (user == null) return -1;
        if (!user.getPassword().equals(password)) return -1;
        String sessionIdStr = sessionService.createSession(user.getId());
        try {
            return Integer.parseInt(sessionIdStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Deletes the user associated with the given session ID.
     * Also removes the session from the session service.
     *
     * @param sessionId the session ID as a string
     */
    public void deleteUser(String sessionId) {
        String userIdStr = sessionService.getUserId(sessionId);
        if (userIdStr == null) return;
        try {
            int userId = Integer.parseInt(userIdStr);
            userStore.deleteById(userId);
        } catch (NumberFormatException e) {
            // ignore
        }
        sessionService.removeSession(sessionId);
    }

}