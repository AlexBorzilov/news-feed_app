package aborzilov.newsfeed.error;

import java.util.List;

import aborzilov.newsfeed.response.BaseSuccessResponse;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseSuccessResponse> handleNotReadableMessageException() {
        return new ResponseEntity<>(new BaseSuccessResponse(
                ErrorCodes.determineErrorCode(ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseSuccessResponse> handleNotSupportedRequestMethodException() {
        return new ResponseEntity<>(new BaseSuccessResponse(ErrorCodes.determineErrorCode(
                ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomSuccessResponse<List<Integer>>> handleException(MissingServletRequestParameterException e) {
        List<Integer> statusCodes = e
                .getBody()
                .getDetail()
                .lines()
                .map(ErrorCodes::determineErrorCode)
                .distinct()
                .toList();
        return new ResponseEntity<>(new CustomSuccessResponse<>(statusCodes, statusCodes.get(0)),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<BaseSuccessResponse> handleFileException(FileUploadException e) {
        return new ResponseEntity<>(new BaseSuccessResponse(
                ErrorCodes.determineErrorCode(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NewsFeedException.class)
    public ResponseEntity<BaseSuccessResponse> handleBusinessException(NewsFeedException e) {
        return new ResponseEntity<>(new BaseSuccessResponse(ErrorCodes.determineErrorCode(
                e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseSuccessResponse> handleValidateException() {
        return new ResponseEntity<>(new BaseSuccessResponse(ErrorCodes.determineErrorCode(
                ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomSuccessResponse<List<Integer>>> handleException(ConstraintViolationException e) {
        List<Integer> statusCodes = e.getConstraintViolations()
                .stream()
                .map(error -> ErrorCodes.determineErrorCode(error.getMessage()))
                .toList();
        return new ResponseEntity<>(new CustomSuccessResponse<>(statusCodes, statusCodes.get(0)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomSuccessResponse<List<Integer>>> handleException(MethodArgumentNotValidException e) {
        List<String> errorCodes = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        List<Integer> statusCodes = errorCodes
                .stream()
                .map(error -> ErrorCodes.determineErrorCode(error))
                .distinct()
                .toList();
        return new ResponseEntity<>(new CustomSuccessResponse<>(statusCodes, statusCodes.get(0)),
                HttpStatus.BAD_REQUEST);
    }
}
