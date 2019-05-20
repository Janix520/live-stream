package fun.stgoder.live.stream.core.exception;

public class StreamException extends Exception {

    private static final long serialVersionUID = 5593352115202287568L;

    private final int code;

    private final String msg;

    public StreamException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public StreamException(int code, Throwable cause) {
        super(cause);
        this.code = code;
        this.msg = cause.getMessage();
    }

    public StreamException(int code, String msg, Throwable cause) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
