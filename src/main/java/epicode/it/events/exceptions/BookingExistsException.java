package epicode.it.events.exceptions;

public class BookingExistsException extends RuntimeException{
    public BookingExistsException() {
        super();
    }

    public BookingExistsException(String message) {
        super(message);
    }
}
