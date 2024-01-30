package aborzilov.newsfeed.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogModel {
    private String method;
    private String uri;
    private int status;
    private String exception;
}
