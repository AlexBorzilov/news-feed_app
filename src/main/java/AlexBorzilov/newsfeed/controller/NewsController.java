package AlexBorzilov.newsfeed.controller;

import AlexBorzilov.newsfeed.dto.NewsDto;
import AlexBorzilov.newsfeed.response.CreateNewsSuccessResponse;
import AlexBorzilov.newsfeed.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Validated
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CreateNewsSuccessResponse> createNews(@RequestBody @Valid NewsDto newsDto) {
        return ResponseEntity.ok(newsService.createNews(newsDto));
    }

    @GetMapping
    public ResponseEntity<?> getNews(@RequestParam int page, @RequestParam int perPage) {
        return ResponseEntity.ok(newsService.getNews(page, perPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserNews(@PathVariable UUID id,
                                         @RequestParam int page,
                                         @RequestParam int perPage) {
        return ResponseEntity.ok(newsService.getUserNews(page, perPage, id));
    }
}
