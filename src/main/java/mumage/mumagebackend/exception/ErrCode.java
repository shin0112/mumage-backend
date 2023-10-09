package mumage.mumagebackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrCode {
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST.value(), "비밀번호 형식이 잘못되었습니다."),
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST.value(), "이름 형식이 잘못되었습니다."),
    INVALID_NICKNAME_FORMAT(HttpStatus.BAD_REQUEST.value(), "닉네임 형식이 잘못되었습니다."),
    DUPLICATED_ID(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 아이디입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 닉네임입니다.");

    private final int status;
    private final String message;
}
