package AlexBorzilov.newsfeed.view;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PublicUserView {
    private String avatar;
    private String email;
    private UUID id;
    private String name;
    private String role;
}
