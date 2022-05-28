package gitjet.model.clonerepo;

/**
 * A class that represents custom exception for cloning.
 */
public class GitCloningException extends Exception {
    /**
     * Constructor from message.
     * @param message Message to display.
     */
    public GitCloningException(String message) {
        super(message);
    }
}