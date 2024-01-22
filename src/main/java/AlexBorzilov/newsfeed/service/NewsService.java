package AlexBorzilov.newsfeed.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import AlexBorzilov.newsfeed.dto.GetNewsOutDto;
import AlexBorzilov.newsfeed.dto.NewsDto;
import AlexBorzilov.newsfeed.error.ValidationConstants;
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
                        tag.setTitle(string);
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

    public CustomSuccessResponse<PageableResponse<GetNewsOutDto>> getNews(@Positive int page,
                                                                          @Positive int perPage) {
        List<GetNewsOutDto> newsEntityList = newsRepo
                .findAll()
                .stream()
                .skip(page - 1)
                .limit(perPage + 1)
                .map(NewsMapper.INSTANCE::NewsEntityToGetNewsOutDto)
                .peek(getNewsOutDto -> {
                    getNewsOutDto.setUserId(newsRepo.findById(getNewsOutDto.getId()).orElseThrow(() ->
                            new NewsFeedException(ValidationConstants.NEWS_ID_NULL)).getUser().getId());

                    getNewsOutDto.setUsername(newsRepo.findById(getNewsOutDto.getId()).orElseThrow(() ->
                            new NewsFeedException(ValidationConstants.NEWS_ID_NULL)).getUser().getName());
                })
                .toList();
        PageableResponse<GetNewsOutDto> response = new PageableResponse<>(newsEntityList, newsEntityList.size());
        return new CustomSuccessResponse<>(response);
    }

    public CustomSuccessResponse<PageableResponse<GetNewsOutDto>> getUserNews(@Positive int page,
                                                                              @Positive int perPage,
                                                                              UUID id) {
        List<GetNewsOutDto> newsEntityList = newsRepo
                .findAll()
                .stream()
                .filter(newsEntity -> newsEntity.getUser().getId().equals(id))
                .skip(page - 1)
                .limit(perPage)
                .map(NewsMapper.INSTANCE::NewsEntityToGetNewsOutDto)
                .peek(getNewsOutDto -> {
                    getNewsOutDto.setUserId(newsRepo.findById(getNewsOutDto.getId()).orElseThrow(() ->
                            new NewsFeedException(ValidationConstants.NEWS_ID_NULL)).getUser().getId());

                    getNewsOutDto.setUsername(newsRepo.findById(getNewsOutDto.getId()).orElseThrow(() ->
                            new NewsFeedException(ValidationConstants.NEWS_ID_NULL)).getUser().getName());
                })
                .toList();
        PageableResponse<GetNewsOutDto> response = new PageableResponse<>(newsEntityList, newsEntityList.size());
        return new CustomSuccessResponse<>(response);
    }
}
