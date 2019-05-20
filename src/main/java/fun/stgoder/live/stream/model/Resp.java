package fun.stgoder.live.stream.model;

import java.io.Serializable;

import fun.stgoder.live.stream.common.Code;

public class Resp implements Serializable {

    private static final long serialVersionUID = 5854452812497639129L;

    private int code;

    private boolean success;

    private String message;

    private Object data;

    public Resp() {
    }

    public Resp(String message) {
        this.message = message;
    }

    public Resp(int code, String message) {
        this.code = code;
        this.message = message;
        if (code == Code.REQUEST_OK)
            this.success = true;
    }

    public Resp(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        if (code == Code.REQUEST_OK)
            this.success = true;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
