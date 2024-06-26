package aborzilov.newsfeed.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import aborzilov.newsfeed.dto.GetNewsOutDto;
import aborzilov.newsfeed.dto.NewsDto;
import aborzilov.newsfeed.entity.NewsEntity;
import aborzilov.newsfeed.entity.TagEntity;
import aborzilov.newsfeed.error.ErrorCodes;
import aborzilov.newsfeed.error.NewsFeedException;
import aborzilov.newsfeed.mappers.NewsMapper;
import aborzilov.newsfeed.repository.NewsRepo;
import aborzilov.newsfeed.repository.NewsSpecificationMaker;
import aborzilov.newsfeed.repository.TagRepo;
import aborzilov.newsfeed.repository.UserRepo;
import aborzilov.newsfeed.response.BaseSuccessResponse;
import aborzilov.newsfeed.response.CreateNewsSuccessResponse;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.response.PageableResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepo newsRepo;
    private final TagRepo tagRepo;
    private final UserRepo userRepo;

    private Set<TagEntity> createTags(Set<String> set) {
        if (set == null) {
            throw new NewsFeedException(ErrorCodes.TAGS_NOT_VALID.getErrorMessage());
        }
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

    public CreateNewsSuccessResponse createNews(NewsDto newsDto, String id) {
        NewsEntity news = NewsMapper.INSTANCE.NewsDtoToNewsEntity(newsDto);
        news.setTags(createTags(newsDto.getTags()));
        news.setUser(userRepo.findById(UUID.fromString(id)).orElseThrow(() ->
                new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage())
        ));
        news = newsRepo.save(news);
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
        if (userRepo.findById(id).isEmpty()) {
            throw new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage());
        }
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

    public BaseSuccessResponse delete(long id, String uuid) {
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

    public BaseSuccessResponse putNews(long id, NewsDto newsDto, String uuid) {
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
