/**
 * validation exception
 *
 * thrown by rules validate when hours, horizon, or lock checks fail
 *
 * @author Rocco Falco
 * @version Nov 10th, 2025
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String msg) {
        super(msg);
    }
}
