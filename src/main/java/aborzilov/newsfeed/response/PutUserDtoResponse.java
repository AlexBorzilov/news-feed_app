package aborzilov.newsfeed.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutUserDtoResponse {
    String avatar;
    String email;
    UUID id;
    String name;
    String role;
}
