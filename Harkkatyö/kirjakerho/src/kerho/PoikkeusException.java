package kerho;

/**
 * Poikkeusluokka.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class PoikkeusException extends Exception {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Poikkeus jolle tuodaan viesti.
     * @param viesti poikkeusviesti
     */
    public PoikkeusException(String viesti) {
        super(viesti);
    }
}