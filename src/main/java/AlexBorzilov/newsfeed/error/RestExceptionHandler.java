package AlexBorzilov.newsfeed.error;

import AlexBorzilov.newsfeed.response.BaseSuccessResponse;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {
    private final boolean success = true;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleNotReadableMessageException() {
        return new ResponseEntity<>(new BaseSuccessResponse(
                ErrorCodes.determineErrorCode(ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION), success),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleNotSupportedRequestMethodException() {
        return new ResponseEntity<>(new CustomSuccessResponse<>(ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION,
                ErrorCodes.determineErrorCode(ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION), success),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(MissingServletRequestParameterException e) {
        List<String> errorList = e
                .getBody()
                .getDetail()
                .lines()
                .toList();
        List<Integer> errorCodeList = errorList
                .stream()
                .map(ErrorCodes::determineErrorCode)
                .distinct()
                .toList();
        return new ResponseEntity<>(new CustomSuccessResponse<>(errorList, errorCodeList.get(0), success),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NewsFeedException.class)
    public ResponseEntity<?> handleBusinessException() {
        return new ResponseEntity<>(new CustomSuccessResponse<>(ValidationConstants.TASK_NOT_FOUND,
                ErrorCodes.determineErrorCode(ValidationConstants.TASK_NOT_FOUND), success), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidateException() {
        return new ResponseEntity<>(new CustomSuccessResponse<>(ValidationConstants.PAGE_SIZE_NOT_VALID,
                ErrorCodes.determineErrorCode(ValidationConstants.PAGE_SIZE_NOT_VALID), success), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleException(ConstraintViolationException e) {
        List<Integer> statusCodes = e.getConstraintViolations()
                .stream()
                .map(error -> ErrorCodes.determineErrorCode(error.getMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new CustomSuccessResponse<>(statusCodes, statusCodes.get(0), true));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleException(MethodArgumentNotValidException e) {
        List<Integer> statusCodes = e.getBindingResult().getAllErrors()
                .stream()
                .map(error -> ErrorCodes.determineErrorCode(error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new CustomSuccessResponse<>(statusCodes, statusCodes.get(0), true));
    }
}
