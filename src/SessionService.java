// Defines how login sessions are handled
/**
* Defines operations for managing user login sessions.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/
public interface SessionService {
    /**
     * Creates a new session for the given user ID.
     *
     * @param userId the ID of the user to create a session for
     * @return the newly created session ID as a String
     */
    String createSession(int userId);
    /**
     * Retrieves the user ID associated with the given session ID.
     *
     * @param sessionId the session ID to look up
     * @return the user ID as a String, or null if the session does not exist
     */
    String getUserId(String sessionId);
    /**
     * Removes the session with the specified session ID.
     *
     * @param sessionId the session ID to remove
     * @return true if the session existed and was removed, false otherwise
     */
    boolean removeSession(String sessionId);
}

