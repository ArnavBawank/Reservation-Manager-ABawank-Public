// Creates a contract for authentication services
/**
* Defines a contract for authentication services.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/
public interface AuthService {
    /**
     * Creates a new user with the given email and password.
     *
     * @param email the email address of the new user
     * @param password the password of the new user
     * @return the user ID if successful, or -1 if creation failed (e.g., email already exists)
     */
    int createUser(String email, String password);
    /**
     * Logs in a user with the specified email and password.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return the session ID as an integer if login is successful, or -1 if login failed
     */
    int login(String email, String password);
    /**
     * Deletes the user associated with the specified session ID.
     *
     * @param sessionId the session ID corresponding to the user to delete
     */
    void deleteUser(String sessionId);
}