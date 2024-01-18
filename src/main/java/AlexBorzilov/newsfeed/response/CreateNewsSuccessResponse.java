package AlexBorzilov.newsfeed.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNewsSuccessResponse {
    private long id;
    private int statusCode = 1;
    private boolean success = true;

    public CreateNewsSuccessResponse(long id, int statusCode) {
        this.id = id;
        this.statusCode = statusCode;
    }
}
