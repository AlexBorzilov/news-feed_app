package AlexBorzilov.newsfeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutUserDto {
    String avatar;
    @Email
    String email;
    @Size(min = 3, max = 25)
    String name;
    @Size(min = 3, max = 25)
    String role;
}
