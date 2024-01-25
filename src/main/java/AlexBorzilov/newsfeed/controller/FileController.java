package AlexBorzilov.newsfeed.controller;


import AlexBorzilov.newsfeed.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Validated
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/uploadFile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }
}
