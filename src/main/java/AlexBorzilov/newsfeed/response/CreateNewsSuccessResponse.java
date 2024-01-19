package AlexBorzilov.newsfeed.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateNewsSuccessResponse {
    private long id;
    private int statusCode = 1;
    private boolean success = true;

    public CreateNewsSuccessResponse(long id) {
        this.id = id;
    }
}
