package airportsProject.Exceptions;

public class WrongTypeFileException extends Throwable{
    public WrongTypeFileException() {
    }

    public WrongTypeFileException(String s) {
        super(s);
    }
}
