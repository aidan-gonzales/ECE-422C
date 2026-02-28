/**
 * Exception thrown when trying to create an invalid entity type.
 * 
 * TODO: This class should extend the appropriate Java exception class.
 *       Think about whether this is a checked or unchecked exception.
 */
public class InvalidEntityException extends RuntimeException {

	// TODO: Implement this exception class
    // default constructor

    /**
     * Calls the default constructor of the superclass
     */
    public InvalidEntityException() {
        super();
    }

    // constructor with message parameter

    /**
     * Calls the constructor with a String parameter of the superclass
     * @param message the message associated with this exception
     */
    public InvalidEntityException(String message) {
        super(message);
    }



}
