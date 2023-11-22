package mumage.mumagebackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank @Size(min = 8, max = 15) @Pattern(regexp = "^[0-9a-zA-Z!@#$%^&+=]*$")
    private String password;
    @NotBlank @Size(min = 2, max = 10) @Pattern(regexp = "^[a-zA-Z]+|[가-힣]+$")
    private String name;
    @NotBlank @Size(min = 1, max = 15) @Pattern(regexp = "^[0-9a-zA-Z가-힣]*$")
    private String nickname;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;
    private String profileUrl;
    private Set<String> genres;

}
