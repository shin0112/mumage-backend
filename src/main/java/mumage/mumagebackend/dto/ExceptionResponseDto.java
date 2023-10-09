package mumage.mumagebackend.dto;

import lombok.Getter;
import mumage.mumagebackend.exception.ErrCode;

@Getter
public class ExceptionResponseDto {
    private final int status;
    private final Object message;

    public ExceptionResponseDto(int status, Object message) {
        this.status = status;
        this.message = message;
    }

    public ExceptionResponseDto(ErrCode errCode) {
        this.message = errCode.getMessage();
        this.status = errCode.getStatus();
    }
}
