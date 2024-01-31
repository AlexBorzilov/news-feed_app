package aborzilov.newsfeed;

import aborzilov.newsfeed.error.ErrorCodes;
import aborzilov.newsfeed.error.NewsFeedException;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.service.FileService;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    private final SoftAssertions softAssertions = new SoftAssertions();
    final String FILE_NAME = "kot.jpg";
    final String FILE_PATH = "src/test/resources/";

    @Test
    void correctUploadFileTest() {
        byte[] content;
        try {
            content = Files.readAllBytes(Paths.get(FILE_PATH + FILE_NAME));
        }
        catch (IOException e) {
            throw new AssertionFailure("problem with file");
        }
        MultipartFile file = new MockMultipartFile("file", FILE_NAME, "image/jpeg", content);

        CustomSuccessResponse<String> response = fileService.uploadFile(file);

        softAssertions.assertThat(response.getData()).isNotNull();
        softAssertions.assertThat(response.getData()).isEqualTo(FILE_NAME);
    }

    @Test
    void incorrectUploadFile() {
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> fileService.uploadFile(null));
        softAssertions
                .assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.UNKNOWN.getErrorMessage());
    }

    @Test
    void incorrectGetFile() {
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> fileService.getFile("NOTHING.jpg"));
        softAssertions
                .assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.UNKNOWN.getErrorMessage());
    }
}
