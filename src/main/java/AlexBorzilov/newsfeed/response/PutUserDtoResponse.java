package AlexBorzilov.newsfeed.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PutUserDtoResponse {

    String avatar;
    String email;
    UUID id;
    String name;
    String role;

}
