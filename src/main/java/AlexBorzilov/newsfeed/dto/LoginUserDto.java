package AlexBorzilov.newsfeed.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class LoginUserDto {
    private String avatar;
    private String email;
    private UUID id;
    private String role;
    private String token;

    public LoginUserDto(String avatar, String email, UUID id, String role, String token) {
        this.avatar = avatar;
        this.email = email;
        this.id = id;
        this.role = role;
        this.token = token;
    }
}
