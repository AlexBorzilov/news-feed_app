package AlexBorzilov.newsfeed.service;

import java.util.*;
import java.util.stream.Collectors;

import AlexBorzilov.newsfeed.dto.GetNewsOutDto;
import AlexBorzilov.newsfeed.dto.NewsDto;
import AlexBorzilov.newsfeed.error.ValidationConstants;
import AlexBorzilov.newsfeed.repository.NewsSpecificationMaker;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.response.PageableResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import AlexBorzilov.newsfeed.entity.NewsEntity;
import AlexBorzilov.newsfeed.entity.TagEntity;
import AlexBorzilov.newsfeed.error.ErrorCodes;
import AlexBorzilov.newsfeed.error.NewsFeedException;
import AlexBorzilov.newsfeed.mappers.NewsMapper;
import AlexBorzilov.newsfeed.repository.NewsRepo;
import AlexBorzilov.newsfeed.repository.TagRepo;
import AlexBorzilov.newsfeed.repository.UserRepo;
import AlexBorzilov.newsfeed.response.CreateNewsSuccessResponse;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepo newsRepo;
    private final TagRepo tagRepo;
    private final UserRepo userRepo;

    private Set<TagEntity> createTags(Set<String> set) {
        return set
                .stream()
                .map(string -> {
                    if (!tagRepo.existsByTitle(string)) {
                        TagEntity tag = new TagEntity();
                        tag.setTitle(string.toLowerCase());
                        tagRepo.save(tag);
                    }
                    return tagRepo.findByTitle(string).orElseThrow(() ->
                            new NewsFeedException(ErrorCodes.TAGS_NOT_VALID.getErrorMessage()));
                }).collect(Collectors.toSet());
    }

    public CreateNewsSuccessResponse createNews(NewsDto newsDto) {
        NewsEntity news = NewsMapper.INSTANCE.NewsDtoToNewsEntity(newsDto);
        news.setTags(createTags(newsDto.getTags()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        news.setUser(userRepo.findById(UUID.fromString(id)).orElseThrow(() ->
                new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage())
        ));
        newsRepo.save(news);
        return new CreateNewsSuccessResponse(news.getId());
    }
    public CustomSuccessResponse<PageableResponse<GetNewsOutDto>> getNews(int page, int perPage) {
        List<GetNewsOutDto> newsEntityList = newsRepo
                .findAll(PageRequest.of(page - 1, perPage))
                .stream()
                .map(this::getNewsOutDto)
                .toList();
        PageableResponse<GetNewsOutDto> response = new PageableResponse<>(newsEntityList, newsEntityList.size());
        return new CustomSuccessResponse<>(response);
    }

    public CustomSuccessResponse<PageableResponse<GetNewsOutDto>> getUserNews(int page, int perPage, UUID id) {
        List<GetNewsOutDto> newsEntityList = newsRepo
                .findAll(PageRequest.of(page-1, perPage))
                .stream()
                .filter(newsEntity -> newsEntity.getUser().getId().equals(id))
                .map(this::getNewsOutDto)
                .toList();
        PageableResponse<GetNewsOutDto> response = new PageableResponse<>(newsEntityList, newsEntityList.size());
        return new CustomSuccessResponse<>(response);
    }

    public PageableResponse<GetNewsOutDto> findNews(String author, String keyWords, int page, int perPage,
                                                    Set<String> tags) {
        List<GetNewsOutDto> dtoList = newsRepo
                .findAll(NewsSpecificationMaker.makeSpec(author, keyWords, tags),
                        PageRequest.of(page - 1, perPage))
                .map(this::getNewsOutDto)
                .toList();
        return new PageableResponse<>(dtoList, dtoList.size());
    }

    private GetNewsOutDto getNewsOutDto(NewsEntity news) {
        GetNewsOutDto getNewsOutDto = NewsMapper.INSTANCE.NewsEntityToGetNewsOutDto(news);
        getNewsOutDto.setUserId(newsRepo.findById(getNewsOutDto.getId()).orElseThrow(() ->
                new NewsFeedException(ValidationConstants.NEWS_ID_NULL)).getUser().getId());
        getNewsOutDto.setUsername(newsRepo.findById(getNewsOutDto.getId()).orElseThrow(() ->
                new NewsFeedException(ValidationConstants.NEWS_ID_NULL)).getUser().getName());
        return getNewsOutDto;
    }
}

