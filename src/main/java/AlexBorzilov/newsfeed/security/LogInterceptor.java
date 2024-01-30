package AlexBorzilov.newsfeed.security;

import java.util.Enumeration;
import java.util.Objects;
import java.util.stream.Stream;

import AlexBorzilov.newsfeed.mappers.LogMapper;
import AlexBorzilov.newsfeed.repository.LogRepo;
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
        String log = "[LOG] " + request.getMethod()
                + " " + request.getRequestURI() + getParameters(request)
                + " status: " + response.getStatus();
        if (e != null) {
            e.printStackTrace();
            log += " [exception: " + e + ']';
        }
        logger.info(log);
        logRepo.save(LogMapper.INSTANCE.logToLogEntity(log));
    }

    private String getParameters(HttpServletRequest request) {
        StringBuilder posted = new StringBuilder();
        Enumeration<String> requestParameterNames = request.getParameterNames();
        if (requestParameterNames != null) {
            posted.append('?');
        }
        Objects.requireNonNull(requestParameterNames)
                .asIterator()
                .forEachRemaining(parameter -> {
                    if (posted.length() > 1) {
                        posted.append('&');
                    }
                    posted
                            .append(parameter)
                            .append('=')
                            .append(Stream
                                    .of("password", "pass", "pwd", "bearer", "authorization")
                                    .anyMatch(value -> parameter
                                            .toLowerCase()
                                            .contains(value)) ? "*****" : request.getParameter(parameter));
                });
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddress;
        if (ip == null) {
            ipAddress = getRemoteAddress(request);
        }
        else {
            ipAddress = ip;
        }
        if (ipAddress != null && !ipAddress.isEmpty()) {
            posted
                    .append("&_psip=")
                    .append(ipAddress);
        }
        return posted.toString();
    }

    private String getRemoteAddress(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && !ipFromHeader.isEmpty()) {
            logger.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}
