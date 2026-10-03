/**
 * section lock model
 *
 * represents a section locked for a specific time slot
 *
 * @author Arnav Bawank
 * @version Nov 10th, 2025
 */
public class SectionLock implements SectionLockInterface {
    private final TimeSlot slot;
    private final String section;

    public SectionLock(TimeSlot slot, String section) {
        this.slot = slot;
        this.section = section;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public String getSection() {
        return section;
    }

    @Override
    public String toString() {
        return "SectionLock{" + "slot=" + slot + ", section='" + section + '\'' + '}';
    }
}
