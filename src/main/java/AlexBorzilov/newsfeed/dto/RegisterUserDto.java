package AlexBorzilov.newsfeed.dto;

import AlexBorzilov.newsfeed.error.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String avatar;
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_NULL)
    @Size(min = 3, max = 100,
            message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;
    @Size(min = 3, max = 25,
    message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    private String name;
    private String password;
    @Size(min = 3, max = 25,
    message = ValidationConstants.ROLE_SIZE_NOT_VALID)
    private String role;
}