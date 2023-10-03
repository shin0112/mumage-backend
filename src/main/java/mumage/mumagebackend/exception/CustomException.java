package mumage.mumagebackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ErrCode errCode;
    private final HttpStatus httpStatus;

    public CustomException(final ErrCode errCode, final HttpStatus httpStatus) {
        super(errCode.getMessage());
        this.errCode = errCode;
        this.httpStatus = httpStatus;
    }


}
