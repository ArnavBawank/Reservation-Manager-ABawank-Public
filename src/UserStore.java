// defines operation for storing and finding users
/**
* Defines operations for storing and retrieving User objects.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/
public interface UserStore {
    /**
     * Saves a user to the storage.
     *
     * @param user the user to save
     */
    void save(User user);
    /**
     * Finds and returns a user by their email address.
     *
     * @param email the email address of the user to find
     * @return the {@link User} object if found, or null if no user exists with the given email
     */
    User findByEmail(String email);
    /**
     * Deletes the user with the specified ID from storage.
     *
     * @param userId the ID of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    boolean deleteById(int userId);
}