package AlexBorzilov.newsfeed.dto;

import AlexBorzilov.newsfeed.error.ErrorCodes;
import AlexBorzilov.newsfeed.error.ValidationConstants;
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
    @Size(min = 3, max = 25, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    String name;
    @Size(min = 3, max = 25, message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    String role;
}
