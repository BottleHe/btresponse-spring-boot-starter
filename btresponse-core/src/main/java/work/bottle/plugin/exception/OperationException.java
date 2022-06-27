package work.bottle.plugin.exception;

public class OperationException extends RuntimeException {

    private int code;
    private Object data;

    private OperationException()
    {
        super();
    }

    public OperationException(int code, String message)
    {
        super(message);
        this.code = code;
        this.data = null;
    }

    public OperationException(int code, String message, Object data)
    {
        super(message);
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
