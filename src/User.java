// defines a user with id, email, and password; makes creating Users more convenient
/**
* Represents a user with an ID, email, and password.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/
public class User implements UserInterface {
    private int id;
    private String email;
    private String password;

    /**
     * Constructs a new User with the given ID, email, and password.
     *
     * @param id the user's unique ID
     * @param email the user's email address
     * @param password the user's password
     */
    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
    /**
     * Returns the user's ID.
     *
     * @return the user ID
     */
    public int getId() { return id; }
    /**
     * Returns the user's email address.
     *
     * @return the user email
     */
    public String getEmail() { return email; }
    /**
     * Returns the user's password.
     *
     * @return the user password
     */
    public String getPassword() { return password; }
}