package AlexBorzilov.newsfeed.error;

public class NewsFeedException extends RuntimeException {
    public NewsFeedException(String message) {
        super(message);
    }
}
