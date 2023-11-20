package mumage.mumagebackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
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

}
