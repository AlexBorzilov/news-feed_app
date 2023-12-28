package AlexBorzilov.newsfeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthDto {

    @Email
    @Size(min = 3, max = 100)
    private String email;
    @NotBlank
    private String password;

    public AuthDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
