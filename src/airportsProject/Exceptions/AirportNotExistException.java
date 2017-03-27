package airportsProject.Exceptions;

public class AirportNotExistException extends Throwable {
    public AirportNotExistException() {
    }

    public AirportNotExistException(String s) {
        super(s);
    }
}
