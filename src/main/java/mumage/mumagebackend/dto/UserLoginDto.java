package mumage.mumagebackend.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserLoginDto {

    @Getter
    @Setter
    public static class Request {
        private String loginId;
        private String password;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Response {
        private String loginId;
        private String JWT;
    }

}
