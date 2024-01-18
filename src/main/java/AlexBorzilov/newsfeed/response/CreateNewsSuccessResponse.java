package AlexBorzilov.newsfeed.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNewsSuccessResponse {
    long id;
    int statusCode;
    boolean success = true;

    public CreateNewsSuccessResponse(int statusCode, long id){
        this.statusCode = statusCode;
        this.id = id;
    }
}
