/**
* A utility class that provides static helper methods for handling authentication commands.
* @author Daniel Gong (gong256)
* @version 2025-11-10
*/
public class AuthCommandHelper implements AuthCommandHelperInterface {
    /** * Formats a command string for creating a new user.
     * <p>
     * The resulting string follows the format: {@code "CREATE_USER [email] [password]"}
     * </p>
     * * @param email the email address of the user to be created
     * @param password the password for the new user
     * @return a formatted String containing the command and arguments separated by spaces
     */
    public static String formatCreateUser(String email, String password) {
        return "CREATE_USER " + email + " " + password;
    }

    /** * Formats a command string for logging in an existing user.
     * <p>
     * The resulting string follows the format: {@code "LOGIN [email] [password]"}
     * </p>
     * * @param email the email address of the user logging in
     * @param password the user's password
     * @return a formatted String containing the command and arguments separated by spaces
     */
    public static String formatLogin(String email, String password) {
        return "LOGIN " + email + " " + password;
    }

    /** * Splits a command line string into its constituent tokens.
     * <p>
     * The input string is split by whitespace, and leading/trailing whitespace is ignored.
     * If the input is null, an empty array is returned.
     * </p>
     * * @param line the command line string to split
     * @return an array of Strings representing the individual tokens in the command
     */
    public static String[] splitCommand(String line) {
        if (line == null) {
            return new String[0];
        }
        return line.trim().split("\\s+");
    }
}
