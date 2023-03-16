package polukhin.Exceptions;
import java.util.InvalidPropertiesFormatException;

public class DuParseException extends InvalidPropertiesFormatException {
    public DuParseException(String message) {
        super(message);
    }
}
