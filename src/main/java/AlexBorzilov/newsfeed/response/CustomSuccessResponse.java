package AlexBorzilov.newsfeed.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomSuccessResponse<T> {
    private T data;
    private T codes;
    private boolean success = true;
    private int statusCode = 0;

    public CustomSuccessResponse(T data) {
        this.data = data;
    }

    public CustomSuccessResponse(T codes, int statusCode) {
        this.codes = codes;
        this.statusCode = statusCode;
    }
}
