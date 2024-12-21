package il.co.mako.infra.service;

public class MakoException extends RuntimeException {

    public MakoException() {
    }

    public MakoException(String message) {
        super(message);
    }

    public MakoException(String message, Throwable cause) {
        super(message, cause);
    }

    public MakoException(Throwable cause) {
        super(cause);
    }

}
