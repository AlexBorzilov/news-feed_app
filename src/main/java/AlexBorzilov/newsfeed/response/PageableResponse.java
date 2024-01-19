package AlexBorzilov.newsfeed.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PageableResponse<T> {
    List<T> content;
    long numberOfElements;

    public PageableResponse(List<T> content, long numberOfElements) {
        this.content = content;
        this.numberOfElements = numberOfElements;
    }
}
