package AlexBorzilov.newsfeed.view;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PublicUserView {
    String avatar;
    String email;
    UUID id;
    String name;
    String role;
}
