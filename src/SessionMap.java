import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
/**
* Simple in-memory implementation of the SessionService interface.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/

public class SessionMap implements SessionService {
    private final Map<String, String> sessions = new ConcurrentHashMap<>();
    private final Random random = new Random();

    /**
     * Creates a new session for the specified user ID.
     * Generates a unique 8-digit session ID.
     *
     * @param userId the ID of the user to create a session for
     * @return the newly generated session ID as a String
     */
    public String createSession(int userId) {
        String sessionId;
        do {
            int sid = 10_000_000 + random.nextInt(90_000_000);
            sessionId = String.valueOf(sid);
        } while (sessions.containsKey(sessionId));

        sessions.put(sessionId, String.valueOf(userId));
        return sessionId;
    }

    /**
     * Retrieves the user ID associated with a given session ID.
     *
     * @param sessionId the session ID to look up
     * @return the user ID as a String, or null if the session does not exist
     */
    public String getUserId(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Removes a session by its session ID.
     *
     * @param sessionId the session ID to remove
     * @return true if the session existed and was removed, false otherwise
     */
    public boolean removeSession(String sessionId) {
        return sessions.remove(sessionId) != null;
    }
}