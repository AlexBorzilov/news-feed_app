package aborzilov.newsfeed;

import java.util.Optional;
import java.util.UUID;

import aborzilov.newsfeed.repository.UserRepo;
import aborzilov.newsfeed.dto.PutUserDto;
import aborzilov.newsfeed.dto.RegisterUserDto;
import aborzilov.newsfeed.entity.UserEntity;
import aborzilov.newsfeed.error.ErrorCodes;
import aborzilov.newsfeed.error.NewsFeedException;
import aborzilov.newsfeed.mappers.UserMapper;
import aborzilov.newsfeed.response.BaseSuccessResponse;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.response.PutUserDtoResponse;
import aborzilov.newsfeed.service.UserService;
import aborzilov.newsfeed.view.PublicUserView;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    SoftAssertions softAssertions = new SoftAssertions();
    private final String ID = "00237da2-6d16-4f90-808e-de374216c923";
    private UserEntity user;


    @BeforeEach
    void setUp() {
        RegisterUserDto registrationRequest = new RegisterUserDto();
        String AVATAR_TEST = "test.jpg";
        registrationRequest.setAvatar(AVATAR_TEST);
        String EMAIL_TEST = "testing@test.ru";
        registrationRequest.setEmail(EMAIL_TEST);
        String NAME_TEST = "test";
        registrationRequest.setName(NAME_TEST);
        String PASSWORD_TEST = "TEST_PASSWORD1111";
        registrationRequest.setPassword(PASSWORD_TEST);
        String ROLE_TEST = "user";
        registrationRequest.setRole(ROLE_TEST);
        user = UserMapper.INSTANCE.registerUserDtoToUserEntity(registrationRequest);
        user.setPassword(new BCryptPasswordEncoder().encode(registrationRequest.getPassword()));
        user.setId(UUID.fromString(ID));
    }

    @Test
    void replaceUserTest() {

        PutUserDto putUserDto = new PutUserDto();
        putUserDto.setAvatar("put.jpg");
        putUserDto.setName("putName");
        putUserDto.setRole("puter");
        putUserDto.setEmail("test-put@test.ru");

        when(userRepo.findById(UUID.fromString(ID)))
                .thenReturn(Optional.ofNullable(user));

        CustomSuccessResponse<PutUserDtoResponse> response = userService.replaceUser(putUserDto, ID);

        softAssertions.assertThat(response).isNotNull();
        softAssertions.assertThat(putUserDto.getAvatar()).isEqualTo(response.getData().getAvatar());
        softAssertions.assertThat(putUserDto.getName()).isEqualTo(response.getData().getName());
        softAssertions.assertThat(putUserDto.getEmail()).isEqualTo(response.getData().getEmail());
        softAssertions.assertThat(putUserDto.getRole()).isEqualTo(response.getData().getRole());
        softAssertions.assertAll();
    }

    @Test
    void incorrectReplaceUserTest() {
        PutUserDto putUserDto = new PutUserDto();
        putUserDto.setAvatar("  ");
        putUserDto.setName("  ");
        putUserDto.setRole(" ");
        putUserDto.setEmail(" ");

        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> userService.replaceUser(putUserDto, ID));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.USER_AVATAR_NOT_NULL);
    }

    @Test
    void deleteUserTest() {
        when(userRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(user));
        Mockito.doNothing().when(userRepo).delete(any(UserEntity.class));
        BaseSuccessResponse response = userService.deleteUser(ID);
        softAssertions
                .assertThat(response.getStatusCode())
                .isEqualTo(1);
    }

    @Test
    void incorrectDeleteUserTest() {
        when(userRepo.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> userService.deleteUser(ID));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.USER_NOT_FOUND);
    }

    @Test
    void incorrectGetInfoUserById() {
        when(userRepo.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> userService.getUserInfoById(UUID.fromString(ID)));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.USER_NOT_FOUND);
    }

    @Test
    void correctGetInfoUser() {
        when(userRepo.findById(UUID.fromString(ID)))
                .thenReturn(Optional.of(user));
        CustomSuccessResponse<PublicUserView> response = userService.getUserInfo(ID);
        softAssertions.assertThat(response).isNotNull();
        softAssertions.assertThat(response.getData().getAvatar()).isEqualTo(user.getAvatar());
        softAssertions.assertThat(response.getData().getName()).isEqualTo(user.getName());
        softAssertions.assertThat(response.getData().getEmail()).isEqualTo(user.getEmail());
        softAssertions.assertThat(response.getData().getRole()).isEqualTo(user.getRole());
        softAssertions.assertAll();
    }

    @Test
    void incorrectGetInfoUser() {
        when(userRepo.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> userService.getUserInfo(ID));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.USER_NOT_FOUND.getErrorMessage());
    }


}
