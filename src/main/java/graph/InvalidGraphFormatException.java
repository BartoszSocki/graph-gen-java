package graph;

public class InvalidGraphFormatException extends Exception {
    public InvalidGraphFormatException(String message) {
        super(InvalidGraphFormatException.class.getSimpleName() + ": " + message);
    }
}
