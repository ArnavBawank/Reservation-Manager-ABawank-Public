import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
* @author Daniel Gong (gong256)
* @version 2025-12-03
* This panel allows users to enter their email and password, log in to an
* existing account, or create a new one.
*
*/
public class LoginPanel extends JPanel implements LoginPanelInterface {

    private ReservationGUI parent;
    public JTextField emailField;
    public JPasswordField passwordField;
    public JLabel statusLabel;

    /**
     * Constructs a new LoginPanel attached to the given parent GUI.
     *
     * @param parent the {@link ReservationGUI} that owns this panel
     */
    public LoginPanel(ReservationGUI parent) {
        this.parent = parent;

        setLayout(new GridLayout(6, 1));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        statusLabel = new JLabel("");

        JButton loginButton = new JButton("Login");
        JButton createButton = new JButton("Create Account");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCreateAccount();
            }
        });

        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
        add(createButton);
        add(statusLabel);
    }

    /**
     * Handles the login process by sending the entered credentials to the server.
     * <p>
     * On success, the session ID is extracted, stored in the parent GUI, and the
     * main menu panel is shown. On failure, an error message is displayed.
     * </p>
     */
    private void handleLogin() {
        String email = emailField.getText();
        String pass = new String(passwordField.getPassword());

        String response = parent.getClient().login(email, pass);

        if (response.startsWith("OK")) {
            int sessionId = Integer.parseInt(response.split(" ")[1]);
            parent.setLoggedIn(sessionId, email);
            clearFields();
            parent.showPanel("mainMenu");
        } else {
            statusLabel.setText(response);
        }
    }

    /**
     * Handles creating a new user account.
     * <p>
     * Sends the email and password to the server. If the account is created
     * successfully, the user is informed. Otherwise, the error is displayed.
     * </p>
     */
    private void handleCreateAccount() {
        String email = emailField.getText();
        String pass = new String(passwordField.getPassword());

        String response = parent.getClient().createAccount(email, pass);

        if (response.startsWith("OK")) {
            statusLabel.setText("Account created. You may now log in.");
        } else {
            statusLabel.setText(response);
        }
    }

    /**
     * Clears all text fields in the login form.
     */
    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    /**
     * Sets the displayed status message on the panel.
     *
     * @param msg the message to display
     */
    public void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    /**
     * Backwards-compatible alias for {@link #setStatus(String)} used by older tests.
     *
     * @param msg the message to display
     */
    public void setStatusMessage(String msg) {
        setStatus(msg);
    }
}
