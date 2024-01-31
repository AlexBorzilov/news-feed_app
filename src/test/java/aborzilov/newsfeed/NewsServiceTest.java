package aborzilov.newsfeed;

import aborzilov.newsfeed.dto.NewsDto;
import aborzilov.newsfeed.entity.NewsEntity;
import aborzilov.newsfeed.entity.TagEntity;
import aborzilov.newsfeed.entity.UserEntity;
import aborzilov.newsfeed.error.ErrorCodes;
import aborzilov.newsfeed.error.NewsFeedException;
import aborzilov.newsfeed.mappers.NewsMapper;
import aborzilov.newsfeed.repository.NewsRepo;
import aborzilov.newsfeed.repository.TagRepo;
import aborzilov.newsfeed.repository.UserRepo;
import aborzilov.newsfeed.response.BaseSuccessResponse;
import aborzilov.newsfeed.response.CreateNewsSuccessResponse;
import aborzilov.newsfeed.service.NewsService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsRepo newsRepo;

    @Mock
    private TagRepo tagRepo;

    @Mock
    private UserRepo userRepo;

    SoftAssertions softAssertions = new SoftAssertions();
    private final String description = "KAKOY-TA TEST TEXT";
    private final String image = "test.jpg";
    private final String title = "ZAGOLOVOK TEST";
    private static final Set<String> tags = Set.of("tag1", "tag2", "tag3", "tag4");

    private static UserEntity user;

    private static UserEntity user2;
    private static String idUser1;
    private static String idUser2;
    private static final String EMAIL_TEST1 = "test_email1";
    private static final String EMAIL_TEST2 = "test_email2";

    private static final String PASSWORD_TEST = "PASSWORD";

    private static final long newsId = 10;
    private NewsEntity news;
    private static TagEntity[] tagEntities = new TagEntity[tags.size()];

    @BeforeAll
    static void setUpBeforeAll() {
        user = new UserEntity();
        user.setAvatar("test.jpg");
        user.setEmail(EMAIL_TEST1);
        user.setName("test");
        user.setRole("user");
        user.setPassword(new BCryptPasswordEncoder().encode(PASSWORD_TEST));
        user.setId(UUID.randomUUID());
        idUser1 = user
                .getId()
                .toString();
        user2 = new UserEntity();
        user2.setAvatar("test.jpg");
        user2.setEmail(EMAIL_TEST2);
        user2.setName("test");
        user2.setRole("user");
        user2.setPassword(new BCryptPasswordEncoder().encode(PASSWORD_TEST));
        user2.setId(UUID.randomUUID());
        idUser2 = user2
                .getId()
                .toString();
        int i = 0;
        for (String tag : tags) {
            tagEntities[i] = new TagEntity();
            tagEntities[i].setId(i);
            tagEntities[i].setTitle(tag);
            i++;
        }
    }

    @BeforeEach
    void setUp() {
        news = new NewsEntity();
        news.setUser(user);
        news.setTitle(title);
        news.setDescription(description);
        news.setImage(image);
        Set<TagEntity> tagEntitySet = Arrays.stream(tagEntities).collect(Collectors.toSet());
        news.setTags(tagEntitySet);
        news.setId(newsId);
    }

    @Test
    void correctCreateNews() {
        NewsDto newsDto = new NewsDto();
        newsDto.setDescription(description);
        newsDto.setTags(tags);
        newsDto.setTitle(title);
        newsDto.setImage(image);
        news = NewsMapper.INSTANCE.NewsDtoToNewsEntity(newsDto);
        news.setId(1L);

        when(userRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(user));
        when(tagRepo.findByTitle(anyString()))
                .thenReturn(Optional.of(tagEntities[0]));
        when(newsRepo.save(any(NewsEntity.class)))
                .thenReturn(news);

        CreateNewsSuccessResponse response = newsService.createNews(newsDto, idUser1);

        softAssertions.assertThat(response.getStatusCode()).isEqualTo(1);
        softAssertions.assertAll();
    }

    @Test
    void incorrectCreateNews() {
        NewsDto newsDto = new NewsDto();
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> newsService.createNews(newsDto, idUser1));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.TAGS_NOT_VALID);
    }

    @Test
    void incorrectGetNews() {
        int page = -1;
        int perPage = -2;
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> newsService.getNews(page, perPage));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.PAGE_SIZE_NOT_VALID.getErrorMessage());
    }

    @Test
    void incorrectGetUserNews() {
        int page = -1;
        int perPage = -2;
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> newsService.getUserNews(page, perPage, UUID.fromString(idUser1)));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.PAGE_SIZE_NOT_VALID.getErrorMessage());
    }

    @Test
    void correctDeleteNewsTest() {
        when(newsRepo.findById(any(Long.class)))
                .thenReturn(Optional.of(news));
        Mockito.doNothing().when(newsRepo).deleteById(anyLong());
        BaseSuccessResponse response = newsService.delete(newsId, idUser1);
        softAssertions
                .assertThat(response.getStatusCode())
                .isEqualTo(1);
    }

    @Test
    void incorrectDeleteNewsTest() {
        when(newsRepo.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> newsService.delete(newsId, idUser1));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.NEWS_NOT_FOUND);
    }

    @Test
    void correctPutNewsTest() {
        NewsDto newsDto = new NewsDto();
        newsDto.setDescription(description + " put");
        newsDto.setTags(tags);
        newsDto.setTitle(title + " put");
        newsDto.setImage(image);
        when(newsRepo.findById(any(Long.class)))
                .thenReturn(Optional.of(news));

        BaseSuccessResponse response = newsService.putNews(newsId, newsDto, idUser1);
        softAssertions.assertThat(response).isNotNull();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(1);
        softAssertions.assertAll();
    }

    @Test
    void incorrectPutNewsTest() {
        NewsDto newsDto = new NewsDto();
        when(newsRepo.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> newsService.putNews(newsId, newsDto, idUser1));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.NEWS_NOT_FOUND.getErrorMessage());
    }
}