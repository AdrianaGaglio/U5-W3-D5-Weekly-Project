package epicode.it.events.exceptions;

public class MissingInformationException extends RuntimeException{
    public MissingInformationException() {
        super();
    }

    public MissingInformationException(String message) {
        super(message);
    }
}
