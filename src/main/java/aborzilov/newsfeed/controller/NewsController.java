package aborzilov.newsfeed.controller;

import aborzilov.newsfeed.dto.GetNewsOutDto;
import aborzilov.newsfeed.dto.NewsDto;
import aborzilov.newsfeed.error.ValidationConstants;
import aborzilov.newsfeed.response.BaseSuccessResponse;
import aborzilov.newsfeed.response.CreateNewsSuccessResponse;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.response.PageableResponse;
import aborzilov.newsfeed.service.NewsService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
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
    public ResponseEntity<CustomSuccessResponse<PageableResponse<GetNewsOutDto>>> getNews(@RequestParam
                                                                                          @Positive(message = ValidationConstants.PAGE_SIZE_NOT_VALID) int page,
                                                                                          @RequestParam
                                                                                          @Positive(message = ValidationConstants.PAGE_SIZE_NOT_VALID) int perPage) {
        return ResponseEntity.ok(newsService.getNews(page, perPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<GetNewsOutDto>>> getUserNews(@PathVariable UUID id,
                                                                                              @RequestParam
                                                                                              @Positive(message = ValidationConstants.PAGE_SIZE_NOT_VALID) int page,
                                                                                              @RequestParam
                                                                                              @Positive(message = ValidationConstants.PAGE_SIZE_NOT_VALID) int perPage) {
        return ResponseEntity.ok(newsService.getUserNews(page, perPage, id));
    }

    @GetMapping("/find")
    public ResponseEntity<PageableResponse<GetNewsOutDto>> findNews(@Nullable @RequestParam String author,
                                                                    @Nullable @RequestParam String keyWords,
                                                                    @RequestParam
                                                                    @Positive(message = ValidationConstants.PAGE_SIZE_NOT_VALID) int page,
                                                                    @RequestParam
                                                                    @Positive(message = ValidationConstants.PAGE_SIZE_NOT_VALID) int perPage,
                                                                    @Nullable @RequestParam Set<String> tags) {
        return ResponseEntity.ok(newsService.findNews(author, keyWords, page, perPage, tags));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> delete(@PathVariable("id") long id) {
        return ResponseEntity.ok(newsService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> putNews(@PathVariable("id") long id,
                                                       @RequestBody @Valid NewsDto newsDto) {
        return ResponseEntity.ok(newsService.putNews(id, newsDto));
    }

}
