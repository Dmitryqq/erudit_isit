package kg.erudit.common.exceptions;

public class PasswordChangeRequiredException extends Exception {
    public PasswordChangeRequiredException(String message) {
        super(message);
    }
}