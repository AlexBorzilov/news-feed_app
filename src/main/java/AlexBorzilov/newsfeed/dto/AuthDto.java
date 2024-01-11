package AlexBorzilov.newsfeed.dto;

import AlexBorzilov.newsfeed.error.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    @Email
    @Size(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    private String email;
    @NotBlank
    private String password;
}