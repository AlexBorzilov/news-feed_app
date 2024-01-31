package aborzilov.newsfeed.dto;

import aborzilov.newsfeed.error.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    @NotBlank(message = ValidationConstants.USER_AVATAR_NOT_NULL)
    private String avatar;
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_NULL)
    @Size(min = 3, max = 100,
            message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;
    @Size(min = 3, max = 25,
    message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT)
    private String name;
    private String password;
    @Size(min = 3, max = 25,
    message = ValidationConstants.USER_ROLE_NOT_VALID)
    @NotBlank(message = "user role mustn't be null")
    private String role;
}
