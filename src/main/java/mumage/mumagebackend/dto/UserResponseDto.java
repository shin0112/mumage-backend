package mumage.mumagebackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mumage.mumagebackend.domain.Genre;

import java.util.Set;

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
    private Set<Genre> genres;

    @Builder

    public UserResponseDto(Long userId, String loginId, String password, String name, String nickname, String email, String profileUrl, Set<Genre> genres) {
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.profileUrl = profileUrl;
        this.genres = genres;
    }
}

