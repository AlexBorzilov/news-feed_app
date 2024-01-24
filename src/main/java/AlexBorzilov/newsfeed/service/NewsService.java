package AlexBorzilov.newsfeed.service;

import java.util.*;
import java.util.stream.Collectors;

import AlexBorzilov.newsfeed.dto.GetNewsOutDto;
import AlexBorzilov.newsfeed.dto.NewsDto;
import AlexBorzilov.newsfeed.error.ValidationConstants;
import AlexBorzilov.newsfeed.repository.NewsSpecificationMaker;
import AlexBorzilov.newsfeed.response.BaseSuccessResponse;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.response.PageableResponse;
import jakarta.persistence.Id;
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
                .map(NewsMapper.INSTANCE::NewsEntityToGetNewsOutDto)
                .toList();
        PageableResponse<GetNewsOutDto> response = new PageableResponse<>(newsEntityList, newsEntityList.size());
        return new CustomSuccessResponse<>(response);
    }

    public CustomSuccessResponse<PageableResponse<GetNewsOutDto>> getUserNews(int page, int perPage, UUID id) {
        List<GetNewsOutDto> newsEntityList = newsRepo
                .findAll(PageRequest.of(page - 1, perPage))
                .stream()
                .filter(newsEntity -> newsEntity.getUser().getId().equals(id))
                .map(NewsMapper.INSTANCE::NewsEntityToGetNewsOutDto)
                .toList();
        PageableResponse<GetNewsOutDto> response = new PageableResponse<>(newsEntityList, newsEntityList.size());
        return new CustomSuccessResponse<>(response);
    }

    public PageableResponse<GetNewsOutDto> findNews(String author, String keyWords, int page, int perPage,
                                                    Set<String> tags) {
        List<GetNewsOutDto> dtoList = newsRepo
                .findAll(NewsSpecificationMaker.makeSpec(author, keyWords, tags),
                        PageRequest.of(page - 1, perPage))
                .map(NewsMapper.INSTANCE::NewsEntityToGetNewsOutDto)
                .toList();
        return new PageableResponse<>(dtoList, dtoList.size());
    }

    public BaseSuccessResponse delete(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = authentication.getName();

        if (newsRepo.findById(id).
                orElseThrow(() ->
                        new NewsFeedException(ErrorCodes.NEWS_NOT_FOUND.getErrorMessage()))
                .getUser().getId().equals(UUID.fromString(uuid))) {
            newsRepo.deleteById(id);
            return new BaseSuccessResponse();
        }
        else {
            throw new NewsFeedException(ErrorCodes.UNAUTHORISED.getErrorMessage());
        }
    }

    public BaseSuccessResponse putNews(long id, NewsDto newsDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = authentication.getName();
        NewsEntity news = newsRepo.findById(id).orElseThrow(() ->
                new NewsFeedException(ErrorCodes.NEWS_NOT_FOUND.getErrorMessage()));
        if (news.getUser().getId().equals(UUID.fromString(uuid))) {
            news = NewsMapper.INSTANCE.NewsDtoToNewsEntity(newsDto);
            newsRepo.save(news);
            return new BaseSuccessResponse();
        }
        else {
            throw new NewsFeedException(ErrorCodes.UNAUTHORISED.getErrorMessage());
        }
    }
}
