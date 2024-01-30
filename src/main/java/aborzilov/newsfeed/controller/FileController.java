package aborzilov.newsfeed.controller;


import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<CustomSuccessResponse<String>> uploadFile(@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }

    @GetMapping(value = "/{fileName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlResource> getFile(@PathVariable("fileName") String fileName) {
        return ResponseEntity.ok(fileService.getFile(fileName));
    }
}
