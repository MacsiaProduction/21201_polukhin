package polukhin.exceptions;

public class FileMissingUncheckedException extends RuntimeException {
    public FileMissingUncheckedException(String s) {
        super(s);
    }
}
