package mumage.mumagebackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDto {

    @NotBlank @Size(min = 5, max = 15) @Pattern(regexp = "^[0-9a-zA-Z]*$")
    private String joinId;
    @NotBlank @Size(min = 8, max = 15) @Pattern(regexp = "^[0-9a-zA-Z!@#$%^&+=]*$")
    private String password;
    @NotBlank @Size(min = 2, max = 10) @Pattern(regexp = "^[a-zA-Z]+|[가-힣]+$")
    private String name;
    @NotBlank @Size(min = 1, max = 15) @Pattern(regexp = "^[0-9a-zA-Z가-힣]*$")
    private String nickname;
    @NotBlank
    private String role;

}
