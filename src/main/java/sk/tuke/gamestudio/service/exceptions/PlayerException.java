package sk.tuke.gamestudio.service.exceptions;

public class PlayerException extends RuntimeException {
    public PlayerException(String message) {
        super(message);
    }

    public PlayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
