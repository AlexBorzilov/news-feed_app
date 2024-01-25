package AlexBorzilov.newsfeed.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

import AlexBorzilov.newsfeed.error.ErrorCodes;
import AlexBorzilov.newsfeed.error.NewsFeedException;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {
    private final String URL_FILE = "http://localhost:8080/api/v1/file/";
    private final String UPLOAD_PATH = "src/main/resources/homework/";

    public CustomSuccessResponse<String> uploadFile(MultipartFile file) {
        Optional.ofNullable(file)
                .ifPresentOrElse(f -> {
                    try {
                        Files.copy(f.getInputStream(), Paths.get(UPLOAD_PATH)
                                        .resolve(Objects.requireNonNull(f.getOriginalFilename())),
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                    catch (IOException e) {
                        throw new NewsFeedException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getErrorMessage());
                    }

                }, () -> {
                    throw new NewsFeedException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getErrorMessage());
                });
        return new CustomSuccessResponse<>(URL_FILE + file.getOriginalFilename());
    }
}
