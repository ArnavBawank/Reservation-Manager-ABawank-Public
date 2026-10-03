/**
 * rules interface
 *
 * validates reservations against business rules
 * throws validation exception if any policy fails
 *
 * @author Arnav Bawank
 * @version Nov 10th, 2025
 */
public interface Rules {
    void validate(String userId, TimeSlot slot, int partySize, Table chosen)
        throws ValidationException;
}
