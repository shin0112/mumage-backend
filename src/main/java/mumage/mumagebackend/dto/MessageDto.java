package mumage.mumagebackend.dto;

import lombok.Data;

@Data
public class MessageDto {

    private int status;
    private String message;
    private Object data;

    public MessageDto() {
        this.status = 404;
        this.message = "접근 실패";
        this.data = null;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
