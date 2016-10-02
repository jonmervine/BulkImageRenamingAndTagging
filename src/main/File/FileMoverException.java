package main.File;

/**
 * Created by Shirobako on 9/3/2016.
 */
public class FileMoverException extends Exception {

    public FileMoverException() {}

    public FileMoverException(String message) {
        super(message);
    }

    public FileMoverException(Throwable cause) {
        super(cause);
    }

    public FileMoverException(String message, Throwable e) {
        super(message, e);
    }
}
