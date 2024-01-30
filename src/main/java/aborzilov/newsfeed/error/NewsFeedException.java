package aborzilov.newsfeed.error;

public class NewsFeedException extends RuntimeException {
    public NewsFeedException(String message) {
        super(message);
    }
}
