import java.util.Optional;

/**
 * config store interface
 *
 * loads and saves configuration data
 * configuration includes hours and section locks
 *
 * @author Arnav Bawank
 * @version Nov 10th, 2025
 */
public interface ConfigStore {
    
    // Saves the configuration
    void save(Config cfg);

    //Loads the current configuration
    Optional<Config> load();

}