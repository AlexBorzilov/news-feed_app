package aborzilov.newsfeed.security;

import java.util.Enumeration;
import java.util.Objects;
import java.util.stream.Stream;

import aborzilov.newsfeed.entity.LogEntity;
import aborzilov.newsfeed.mappers.LogMapper;
import aborzilov.newsfeed.model.LogModel;
import aborzilov.newsfeed.repository.LogRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    private final LogRepo logRepo;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception e) {
        LogModel logModel = new LogModel();
        logModel.setMethod(request.getMethod());
        logModel.setUri(request.getRequestURI());
        logModel.setStatus(response.getStatus());
        String log = "[LOG] " + logModel.getMethod() + " " + logModel.getUri() + " status: " + logModel.getStatus();
        if (e != null) {
            e.printStackTrace();
            logModel.setException(String.valueOf(e));
            log += " [exception: " + logModel.getException() + ']';
        }
        logger.info(log);
        logRepo.save(LogMapper.INSTANCE.logToLogModelEntity(logModel));
    }
}
