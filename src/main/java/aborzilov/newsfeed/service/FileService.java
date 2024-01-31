package aborzilov.newsfeed.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

import aborzilov.newsfeed.error.ErrorCodes;
import aborzilov.newsfeed.error.NewsFeedException;
import aborzilov.newsfeed.response.CustomSuccessResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${url.file}")
    private String URL_FILE = "http://localhost:8080/api/v1/file/";
    private String UPLOAD_PATH = "src/main/resources/homework/";

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

    public UrlResource getFile(String fileName) {
        try {

            URL file = ResourceUtils.getFile(UPLOAD_PATH + fileName).getAbsoluteFile().toURI().toURL();
            if (!Files.exists(Path.of(file.getPath()))) {
                throw new NewsFeedException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getErrorMessage());
            }
            return new UrlResource(file);
        }
        catch (FileNotFoundException | MalformedURLException e) {
            throw new NewsFeedException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getErrorMessage());
        }
    }
}
