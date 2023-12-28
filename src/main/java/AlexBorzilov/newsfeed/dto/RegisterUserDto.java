package AlexBorzilov.newsfeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDto {
    private String avatar;
    @Size(min = 3, max = 100)
    @Email
    private String email;
    @Size(min = 3, max = 25)
    private String name;
    private String password;
    @Size(min = 3, max = 25)
    private String role;

    public RegisterUserDto(String avatar, String email, String name, String password, String role) {
        this.avatar = avatar;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }
}