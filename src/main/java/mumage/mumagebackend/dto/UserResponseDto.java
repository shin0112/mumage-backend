package mumage.mumagebackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long userId;
    private String loginId;
    private String password;
    private String name;
    private String nickname;
    private String email;
    private String profileUrl;

    @Builder
    public UserResponseDto(Long userId, String loginId, String password, String name, String nickname, String email, String profileUrl) {
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.profileUrl = profileUrl;
    }

}

