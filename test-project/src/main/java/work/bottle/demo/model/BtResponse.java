package work.bottle.demo.model;

import work.bottle.demo.utils.JsonUtils;

public class BtResponse {

    private boolean success;
    private int code;
    private Object data;
    private String message;

    public BtResponse() {
        this(true, 0, null, "");
    }

    public BtResponse(int code, Object data) {
        this(code == 0, code, data, "");
    }

    public BtResponse(int code, String message) {
        this(code == 0, code, null, message);
    }

    public BtResponse(int code, Object data, String message) {
        this(code == 0, code, data, message);
    }

    public BtResponse(boolean success, int code, Object data, String message) {
        this.success = success;
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {

        return new StringBuilder("{\"success\": ")
                .append(success)
                .append(", \"code\": ")
                .append(code)
                .append(", \"message\": \"")
                .append(message)
                .append("\", \"data\": \"")
                .append(JsonUtils.toJsonString(data))
                .append("\"}").toString();
    }
}
