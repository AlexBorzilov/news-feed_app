package aborzilov.newsfeed.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PageableResponse<T> {
    private List<T> content;
    private long numberOfElements;

    public PageableResponse(List<T> content, long numberOfElements) {
        this.content = content;
        this.numberOfElements = numberOfElements;
    }
}
