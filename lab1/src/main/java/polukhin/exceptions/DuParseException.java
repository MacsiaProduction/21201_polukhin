package polukhin.exceptions;


// CR: extend Exception instead
public class DuParseException extends Throwable {
    public DuParseException(String message) {
        super(message);
    }
}
