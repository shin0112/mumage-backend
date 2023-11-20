package mumage.mumagebackend.dto;

import lombok.Builder;
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

    @Builder
    public MessageDto(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
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
