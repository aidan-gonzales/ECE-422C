/**
 * Exception thrown when trying to create an invalid entity type.
 * 
 * TODO: This class should extend the appropriate Java exception class.
 *       Think about whether this is a checked or unchecked exception.
 */
public class InvalidEntityException extends RuntimeException {

	// TODO: Implement this exception class
    // default constructor
    public InvalidEntityException() {
        super();
    }

    // constructor with message parameter
    public InvalidEntityException(String message) {
        super(message);
    }



}
