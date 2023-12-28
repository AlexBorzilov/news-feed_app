package AlexBorzilov.newsfeed.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseSuccessResponse {
    private int statusCode = 1;
    private boolean success = true;

    public BaseSuccessResponse(int statusCode, boolean success) {
        this.statusCode = statusCode;
        this.success = success;
    }
}
